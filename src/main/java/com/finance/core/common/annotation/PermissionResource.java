package com.finance.core.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({PARAMETER})
@Retention(RUNTIME)
public @interface PermissionResource {

    String resourceType();

    /**
     * @return Empty string if this is the resource id. Otherwise, return memeber path to get id. For example: user.id
     */
    String idPath() default "";
}