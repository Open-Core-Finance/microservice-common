package tech.corefinance.common.test.support.controllers;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.corefinance.common.controller.CrudController;
import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.service.CommonService;

@RestController
@RequestMapping(value = "/test")
public class TestController implements CrudController {

    @RequestMapping(value = "/api/jsonerror")
    public boolean jsonerror() throws Exception {
        throw new JsonGenerationException("test-only", (JsonGenerator) null);
    }

    @RequestMapping(value = "/api/serviceErrorWithMessage")
    public boolean serviceErrorWithMessage(@RequestParam(name = "errorMessage", required = false) String errorMessage) throws Exception {
        if (errorMessage != null) {
            throw new ServiceProcessingException(errorMessage, new JsonGenerationException("", (JsonGenerator) null));
        } else {
            throw new ServiceProcessingException((String) null, new JsonGenerationException("", (JsonGenerator) null));
        }
    }

    @RequestMapping(value = "/api/serviceErrorWithMessageNoCause")
    public boolean serviceErrorWithMessageNoCause(@RequestParam(name = "errorMessage", required = false) String errorMessage)
            throws Exception {
        if (errorMessage != null) {
            throw new ServiceProcessingException(errorMessage);
        } else {
            throw new ServiceProcessingException((String) null);
        }
    }

    @RequestMapping(value = "/api/runtimeException")
    public boolean runtimeException() throws Exception {
        throw new RuntimeException();
    }

    @RequestMapping(value = "/api/illegalArgumentException")
    public boolean illegalArgumentException(@RequestParam(value = "msg", required = false) String message) throws Exception {
        throw new IllegalArgumentException(message);
    }

    @RequestMapping(value = "/api/accessDenied")
    public boolean accessDenied(@RequestParam(value = "msg", required = false) String message) throws Exception {
        throw new AccessDeniedException(message);
    }

    @RequestMapping(value = "/normal")
    public boolean normal() throws Exception {
        return true;
    }

    @Override
    public CommonService getHandlingService() {
        return null;
    }
}
