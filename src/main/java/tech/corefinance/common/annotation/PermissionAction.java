package tech.corefinance.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Permission configuration for an API.
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface PermissionAction {

    /**
     * Permission Action of this API.
     * @return Action name
     */
    String action() default "";

    /**
     * Resource type that need to check permission.
     * @return Resource type name.
     */
    String resourceType() default "";

}
