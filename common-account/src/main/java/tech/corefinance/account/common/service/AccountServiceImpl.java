package tech.corefinance.account.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import tech.corefinance.account.common.entity.Account;
import tech.corefinance.common.context.JwtContext;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.jpa.repository.DbSequenceHandling;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.common.util.RandomString;
import tech.corefinance.product.common.model.ProductNewAccountSetting;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
public abstract class AccountServiceImpl<T extends Account, R extends CommonResourceRepository<T, String>>
        implements AccountService<T, R> {

    protected Random random = new Random();
    protected int maxRandomIdCheck;
    protected TaskExecutor taskExecutor;

    protected DbSequenceHandling dbSequenceHandling;

    public AccountServiceImpl(int maxRandomIdCheck, TaskExecutor taskExecutor, DbSequenceHandling dbSequenceHandling) {
        this.maxRandomIdCheck = maxRandomIdCheck;
        this.taskExecutor = taskExecutor;
        this.dbSequenceHandling = dbSequenceHandling;
    }

    protected abstract Object getCategoryObject(String categoryId);

    protected abstract Object getTypeObject(String typeId);

    protected abstract Object getProductObject(String productId);

    protected abstract String getSequenceName();

    @Override
    public <D extends CreateUpdateDto<String>> T copyAdditionalPropertiesFromDtoToEntity(D source, T dest) {
        dest = AccountService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        if (!StringUtils.hasText(dest.getId())) {
            dest.setCreatedDate(ZonedDateTime.now());
            dest.setLastModifiedDate(ZonedDateTime.now());
        }
        var productId = dest.getProductId();
        log.debug("Getting product [{}]...", productId);
        var productResult = getProductObject(productId);
        log.debug("Product [{}]", productResult);
        return mapProductToAccount(productResult, dest);
    }

    private Object retrieveFieldName(Object obj, String fieldName) {
        if (obj != null) {
            var field = ReflectionUtils.findField(obj.getClass(), fieldName);
            if (field != null) {
                field.setAccessible(true);
                try {
                    return field.get(obj);
                } catch (IllegalAccessException e) {
                    throw new ServiceProcessingException(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    private String generateAccountId(ProductNewAccountSetting newAccountSetting) {
        return switch (newAccountSetting.getType()) {
            case INCREASEMENT -> generateIncrementalId(newAccountSetting);
            case RANDOM_PATTERN -> generateRandomId(newAccountSetting);
            default -> UUID.randomUUID().toString();
        };
    }

    private String generateIncrementalId(ProductNewAccountSetting newAccountSetting) {
        var sequenceName = getSequenceName();
        log.debug("Generating incremental ID from sequence [{}]...", sequenceName);
        var currentStarting = dbSequenceHandling.getCurrentSequenceValue(sequenceName);
        log.debug("Current sequence [{}]", currentStarting);
        if (currentStarting < newAccountSetting.getIncreasementStartingFrom()) {
            dbSequenceHandling.restartSequence(newAccountSetting.getIncreasementStartingFrom(), sequenceName);
            log.debug("Restarted new value [{}]", newAccountSetting.getIncreasementStartingFrom());
        }
        currentStarting = dbSequenceHandling.getNextSequenceValue(sequenceName);
        log.debug("New sequence value [{}]", currentStarting);
        var result = new StringBuilder(currentStarting + "");
        if (newAccountSetting.isFixLengthId()) {
            var accountLength = result.length();
            log.debug("Trying to append length to [{}]", accountLength);
            if (accountLength > newAccountSetting.getFixAccountLength()) {
                throw new ServiceProcessingException("invalid_product_setting_account_length");
            }
            while (accountLength < newAccountSetting.getFixAccountLength()) {
                result.insert(0, "0");
                accountLength = result.length();
            }
            log.debug("Finished append 0 and final is [{}]", result);
        }
        if (newAccountSetting.getIdPrefix() != null) {
            result.insert(0, newAccountSetting.getIdPrefix());
            log.debug("Append prefix [{}]", result);
        }
        if (newAccountSetting.getIdSuffix() != null) {
            result.append(newAccountSetting.getIdSuffix());
            log.debug("Append suffix [{}]", result);
        }
        log.debug("Final account ID [{}]", result);
        return result.toString();
    }

    private String generateRandomId(ProductNewAccountSetting newAccountSetting) {
        var repository = getRepository();
        log.debug("Generating random ID...");
        var result = nextRandomId(newAccountSetting);
        log.debug("Generated random ID [{}]", result);
        var duplicatedCount = 0;
        while (repository.existsById(result)) {
            log.debug("Generated random ID [{}] is existed!", result);
            if (duplicatedCount >= maxRandomIdCheck) {
                throw new ServiceProcessingException("account_id_gen_duplicated");
            }
            duplicatedCount++;
            log.debug("Generated random ID [{}]", result);
            result = nextRandomId(newAccountSetting);
        }
        log.debug("Final account ID [{}]", result);
        return result;
    }

    private String nextRandomId(ProductNewAccountSetting newAccountSetting) {
        StringBuilder result = new StringBuilder();
        var template = newAccountSetting.getRandomPatternTemplate();
        var charRandom = new RandomString(1, random, RandomString.upper);
        var numRandom = new RandomString(1, random, RandomString.digits);
        var alphaNumRandom = new RandomString(1, random, RandomString.upper + RandomString.digits);
        for (var i = 0; i < template.length(); i++) {
            var c = template.charAt(i);
            result.append(
                    switch (c) {
                        case '@' -> charRandom.nextString();
                        case '#' -> numRandom.nextString();
                        case '$' -> alphaNumRandom.nextString();
                        default -> c;
                    }
            );
        }
        return result.toString();
    }

    protected T mapProductToAccount(Object productObject, T dest) {
        var newAccountSetting = (ProductNewAccountSetting) retrieveFieldName(productObject, "newAccountSetting");
        log.debug("New Account setting [{}]", newAccountSetting);
        if (!StringUtils.hasText(dest.getId())) {
            dest.setCreatedBy(null);
            if (newAccountSetting != null) {
                dest.setStatus(newAccountSetting.getInitialState());
                dest.setId(generateAccountId(newAccountSetting));
            }
        }

        JwtTokenDto jwtTokenDto = JwtContext.getInstance().getJwt();
        List<CompletableFuture<?>> tasks = new LinkedList<>();
        tasks.add(CompletableFuture.supplyAsync(() -> {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            var categoryId = (String) retrieveFieldName(productObject, "category");
            dest.setCategoryId(categoryId);
            log.debug("Getting category [{}]...", categoryId);
            var categoryResult = getCategoryObject(categoryId);
            log.debug("Category [{}]", categoryResult);
            var name = retrieveFieldName(categoryResult, "name");
            log.debug("Retrieved name [{}]", name);
            if (name != null) {
                dest.setCategoryName((String) name);
                log.debug("Finished setting name [{}] to result", name);
            }
            return categoryResult;
        }, taskExecutor));
        tasks.add(CompletableFuture.supplyAsync(() -> {
            JwtContext.getInstance().setJwt(jwtTokenDto);
            var typeId = (String) retrieveFieldName(productObject, "type");
            dest.setTypeId(typeId);
            log.debug("Getting type [{}]...", typeId);
            var typeResult = getTypeObject(typeId);
            log.debug("Type [{}]", typeResult);
            var name = retrieveFieldName(typeResult, "name");
            log.debug("Retrieved name [{}]", name);
            if (name != null) {
                dest.setTypeName((String) name);
                log.debug("Finished setting name [{}] to result", name);
            }
            return typeResult;
        }, taskExecutor));
        for (var task : tasks) {
            task.join();
        }
        return dest;
    }
}
