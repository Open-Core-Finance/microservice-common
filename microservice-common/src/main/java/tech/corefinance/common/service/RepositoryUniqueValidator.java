package tech.corefinance.common.service;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.ReflectionUtils;
import tech.corefinance.common.annotation.UniqueField;
import tech.corefinance.common.context.ApplicationContextHolder;
import tech.corefinance.common.ex.ReflectiveIncorrectFieldException;
import tech.corefinance.common.model.GenericModel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
public class RepositoryUniqueValidator implements ConstraintValidator<UniqueField, GenericModel<?>> {

    private UniqueField uniqueField;
    private PagingAndSortingRepository<?, ?> repository;

    @Override
    public void initialize(UniqueField constraintAnnotation) {
        this.uniqueField = constraintAnnotation;
    }

    @Override
    public boolean isValid(GenericModel<?> value, ConstraintValidatorContext context) {
        ApplicationContext applicationContext = ApplicationContextHolder.getInstance().getApplicationContext();
        repository = (PagingAndSortingRepository<?, ?>) applicationContext.getBean(uniqueField.repositoryBeanName());
        try {
            Boolean result;
            // Field value
            Field field = ReflectionUtils.findRequiredField(value.getClass(), uniqueField.fieldName());
            field.setAccessible(true);
            Object fieldValue = field.get(value);
            // Method
            Method method;
            Class<?> idType = uniqueField.idType();
            if (void.class.equals(idType)) {
                method = ReflectionUtils.findRequiredMethod(repository.getClass(), uniqueField.repoMethodName(), uniqueField.fieldType());
                result = !((Boolean) method.invoke(repository, fieldValue));
                log.info("Validated [{}] with field [{}]=[{}] and result id [{}]", value.getClass().getName(), uniqueField.fieldName(),
                        fieldValue, result ? "valid" : "invalid");
            } else {
                Object idValue = value.getId();
                method = ReflectionUtils.findRequiredMethod(repository.getClass(), uniqueField.repoMethodName(), uniqueField.fieldType(), idType);
                result = !((Boolean) method.invoke(repository, fieldValue, idValue));
                log.info("Validated [{}] with field [{}]=[{}] and ID=[{}] and result id [{}]", value.getClass().getName(),
                        uniqueField.fieldName(), fieldValue, idValue, result ? "valid" : "invalid");
            }

            return result;
        } catch (ReflectiveOperationException e) {
            throw new ReflectiveIncorrectFieldException(e);
        }
    }

}
