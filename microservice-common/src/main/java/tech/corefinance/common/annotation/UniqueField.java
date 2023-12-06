package tech.corefinance.common.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import tech.corefinance.common.service.RepositoryUniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Define unique field for an entity.
 */
@Documented
@Constraint(validatedBy = {RepositoryUniqueValidator.class})
@Target({TYPE})
@Retention(RUNTIME)
@Repeatable(UniqueField.List.class)
public @interface UniqueField {

    String message() default "field_empty";

    String repoMethodName();
    String repositoryBeanName() default "commonRepository";
    Class<?> idType() default String.class;
    String fieldName();
    Class<?> fieldType() default String.class;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * Defines several {@code @NotEmpty} constraints on the same element.
     *
     * @see jakarta.validation.constraints.NotEmpty
     */
    @Target({TYPE})
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        UniqueField[] value();
    }

}
