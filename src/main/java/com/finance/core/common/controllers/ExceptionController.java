package com.finance.core.common.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.core.common.dto.GeneralApiResponse;
import com.finance.core.common.ex.ResourceNotFound;
import com.finance.core.common.ex.ServiceProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.xml.parsers.ParserConfigurationException;

@ControllerAdvice
@ResponseBody
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ExceptionController extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(value = {ServiceProcessingException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public Object handleProcessingException(HttpServletRequest request, Exception e) {
        return handleGeneralException(request, e, 1);
    }

    @ExceptionHandler(value = {ParserConfigurationException.class, JsonProcessingException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public String handleJsonException(Exception e) {
        LOGGER.error("Parse exception", e);
        return "{\"status\": 1, \"statusCode\": \"invalid_input_or_response\", \"result\": {}}";
    }

    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public GeneralApiResponse<String> accessDenied(AccessDeniedException e) {
        LOGGER.debug(e.getMessage(), e);
        return new GeneralApiResponse<>("access_denied", 1, e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class,ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralApiResponse<String> badRequest(Exception e) {
        LOGGER.debug(e.getMessage(), e);
        if(e instanceof IllegalArgumentException) {
            return new GeneralApiResponse<>("bad_request", 1, e.getMessage());
        }
        if(e instanceof ConstraintViolationException){
            return new GeneralApiResponse<>("bad_request", 1, e.getMessage());
        }
        return new GeneralApiResponse<>("system_error", 1,null);
    }

    @ExceptionHandler(value = {Throwable.class})
    public GeneralApiResponse<String> internalServerError(Throwable e) {
        LOGGER.error("System Exception", e);
        return new GeneralApiResponse<>("system_error", 1, e.getMessage());
    }

    @ExceptionHandler(value = {ResourceNotFound.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public Object handleResourceNotFound(HttpServletRequest request, Exception e) {
        return handleGeneralException(request, e, 404);
    }

    private Object handleGeneralException(HttpServletRequest request, Exception e, int status){
        String message = e.getMessage();
        Throwable cause = e.getCause();
        LOGGER.error("Return error for user: [{}]", message);
        if (cause != null) {
            LOGGER.error("Exception", cause);
        } else {
            cause = e;
        }
        if (message != null && !message.trim().isEmpty()) {
            if (!message.contains("\"statusCode\"")) {
                return new GeneralApiResponse<>(message, status, null);
            } else {
                return message;
            }
        } else {
            return internalServerError(cause);
        }
    }
}