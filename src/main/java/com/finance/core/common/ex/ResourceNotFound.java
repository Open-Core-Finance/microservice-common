package com.finance.core.common.ex;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.core.common.dto.GeneralApiResponse;

public class ResourceNotFound extends RuntimeException{
    private static final long serialVersionUID = -2963416786314466369L;
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
