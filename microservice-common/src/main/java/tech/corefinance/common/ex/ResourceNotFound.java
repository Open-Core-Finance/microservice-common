package tech.corefinance.common.ex;

import tech.corefinance.common.dto.GeneralApiResponse;
import tools.jackson.databind.json.JsonMapper;

public class ResourceNotFound extends RuntimeException {
    private static final long serialVersionUID = -2963416786314466379L;

    public ResourceNotFound(String message, Throwable origin) {
        super(message, origin);
    }

    public ResourceNotFound(String message) {
        super(message);
    }

    public ResourceNotFound(GeneralApiResponse<?> apiResponse) {
        this(new JsonMapper().writeValueAsString(apiResponse));
    }

    public ResourceNotFound(GeneralApiResponse<?> apiResponse, Throwable origin) {
        this(new JsonMapper().writeValueAsString(apiResponse), origin);
    }
}
