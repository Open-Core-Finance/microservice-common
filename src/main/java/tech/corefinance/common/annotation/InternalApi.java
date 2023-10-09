package tech.corefinance.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * To show that this API for internal use only.
 */
@Documented
@Target({METHOD, TYPE})
@Retention(RUNTIME)
public @interface InternalApi {

    /**
     * Defined that this API need authen token pass from the original service or not.
     * @return True if required.
     */
    boolean needAuthenToken() default false;

}
