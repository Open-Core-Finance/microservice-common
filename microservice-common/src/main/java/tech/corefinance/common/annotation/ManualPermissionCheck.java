package tech.corefinance.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Skip permission verify if API marked by this annotation.
 */
@Documented
@Target({METHOD})
@Retention(RUNTIME)
public @interface ManualPermissionCheck {
}
