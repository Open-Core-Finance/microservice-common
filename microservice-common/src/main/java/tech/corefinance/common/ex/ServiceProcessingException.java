package tech.corefinance.common.ex;

import tech.corefinance.common.dto.GeneralApiResponse;
import tools.jackson.databind.json.JsonMapper;

public class ServiceProcessingException extends RuntimeException {

    /**
     * Generated.
     */
    private static final long serialVersionUID = -2963416786594466469L;

    /**
     * Should not use this constructor as general cases. Just use for special case only.
     *
     * @param message Error message
     * @param origin  Origin exception
     */
    public ServiceProcessingException(String message, Throwable origin) {
        super(message, origin);
    }

    /**
     * Create exception with error message
     *
     * @param message Error message
     */
    public ServiceProcessingException(String message) {
        super(message);
    }

    /**
     * Create exception with error message
     *
     * @param apiResponse Error message
     */
    public ServiceProcessingException(GeneralApiResponse<?> apiResponse) {
        this(new JsonMapper().writeValueAsString(apiResponse));
    }

    /**
     * Create exception with error message
     *
     * @param apiResponse Error message
     */
    public ServiceProcessingException(GeneralApiResponse<?> apiResponse, Throwable origin) {
        this(new JsonMapper().writeValueAsString(apiResponse), origin);
    }
}
