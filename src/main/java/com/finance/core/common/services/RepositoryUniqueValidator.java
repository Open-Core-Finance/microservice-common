package com.finance.core.common.services;

import com.finance.core.common.context.ApplicationContextHolder;
import com.finance.core.common.model.GenericModel;
import com.finance.core.common.annotation.UniqueField;
import com.finance.core.common.ex.ReflectiveIncorrectFieldException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RepositoryUniqueValidator implements ConstraintValidator<UniqueField, GenericModel<?>> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
                logger.info("Validated [{}] with field [{}]=[{}] and result id [{}]", value.getClass().getName(), uniqueField.fieldName(),
                        fieldValue, result ? "valid" : "invalid");
            } else {
                Object idValue = value.getId();
                method = ReflectionUtils.findRequiredMethod(repository.getClass(), uniqueField.repoMethodName(), uniqueField.fieldType(), idType);
                result = !((Boolean) method.invoke(repository, fieldValue, idValue));
                logger.info("Validated [{}] with field [{}]=[{}] and ID=[{}] and result id [{}]", value.getClass().getName(),
                        uniqueField.fieldName(), fieldValue, idValue, result ? "valid" : "invalid");
            }

            return result;
        } catch (ReflectiveOperationException e) {
            throw new ReflectiveIncorrectFieldException(e);
        }
    }

}
