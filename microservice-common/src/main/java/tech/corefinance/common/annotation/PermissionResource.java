package tech.corefinance.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Define resource info of an API.
 */
@Documented
@Target({PARAMETER})
@Retention(RUNTIME)
public @interface PermissionResource {

    /**
     * Resource type for this API. <br/>
     * Need to work with ResourceOwnerVerifier to verify if user own or member of this resource type.
     * @return Resource type name.
     */
    String resourceType();

    /**
     * @return Empty string if this is the resource id. Otherwise, return member path to get id. For example: user.id
     */
    String idPath() default "";
}
