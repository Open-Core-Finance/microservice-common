package tech.corefinance.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Define permission based resource name for a controller.
 */
@Documented
@Target({TYPE})
@Retention(RUNTIME)
public @interface ControllerManagedResource {
    /**
     * @return Resource name.
     */
    String value();
}
