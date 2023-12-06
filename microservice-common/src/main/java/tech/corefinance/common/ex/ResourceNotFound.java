package tech.corefinance.common.ex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.corefinance.common.dto.GeneralApiResponse;

public class ResourceNotFound extends RuntimeException{
    private static final long serialVersionUID = -2963416786314466379L;
    public ResourceNotFound(String message, Throwable origin) {
        super(message, origin);
    }

    public ResourceNotFound(String message) {super(message);}
    public ResourceNotFound(GeneralApiResponse<?> apiResponse) throws JsonProcessingException {
        this(new ObjectMapper().writeValueAsString(apiResponse));
    }
    public ResourceNotFound(GeneralApiResponse<?> apiResponse, Throwable origin) throws JsonProcessingException {
        this(new ObjectMapper().writeValueAsString(apiResponse), origin);
    }
}
