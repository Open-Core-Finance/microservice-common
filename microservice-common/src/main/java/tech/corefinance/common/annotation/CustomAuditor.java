package tech.corefinance.common.annotation;

import tech.corefinance.common.enums.CustomAuditorField;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface CustomAuditor {

    CustomAuditorField createdByType() default CustomAuditorField.BASIC_USER_JSON;

    CustomAuditorField lastModifiedByType() default CustomAuditorField.BASIC_USER_JSON;

}
