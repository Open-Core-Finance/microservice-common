package tech.corefinance.common.ex;

public class ReflectiveIncorrectFieldException extends RuntimeException {

    /**
     * Generated.
     */
    private static final long serialVersionUID = -2963416786594345389L;

    /**
     * Should not use this constructor as general cases. Just use for special case only.
     *
     * @param message Error message
     * @param origin  Origin exception
     */
    public ReflectiveIncorrectFieldException(String message, Throwable origin) {
        super(message, origin);
    }

    /**
     * Create exception with error message
     *
     * @param message Error message
     */
    public ReflectiveIncorrectFieldException(String message) {
        super(message);
    }

    /**
     * Create exception with error message
     *
     * @param origin Origin exception
     */
    public ReflectiveIncorrectFieldException(Throwable origin) {
        super(origin);
    }

}
