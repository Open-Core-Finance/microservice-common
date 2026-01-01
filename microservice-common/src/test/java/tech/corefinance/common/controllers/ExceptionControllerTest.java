package tech.corefinance.common.controllers;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tech.corefinance.common.repository.ResourceActionRepository;
import tech.corefinance.common.test.support.app.TestCommonApplication;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = TestCommonApplication.class)
@WebAppConfiguration
@ActiveProfiles({"common", "default", "unittest"})
public class ExceptionControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockitoBean
    private ResourceActionRepository resourceActionRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        PowerMockito.when(resourceActionRepository.saveAll(Mockito.any())).thenReturn(new LinkedList<>());
    }

    @Test
    public void test_handleJsonException() throws Exception {
        mockMvc.perform(get("/test/api/jsonerror")).andExpect((result) -> {
            MockHttpServletResponse response = result.getResponse();
            assertEquals("{\"status\": 1, \"statusCode\": \"invalid_input_or_response\", " +
                    "\"result\": [\"invalid_input_or_response\"]}", response.getContentAsString());
        });
    }

    @Test
    public void test_handleProcessingException_HaveMessageAndOrigin() throws Exception {
        String message = "this is message";
        mockMvc.perform(get("/test/api/serviceErrorWithMessage").param("errorMessage", message)).andExpect((result) -> {
            MockHttpServletResponse response = result.getResponse();
            assertEquals("{\"result\":[\"" + message + "\"],\"status\":1,\"statusCode\":\"this is message\"}", response.getContentAsString());
        });
    }

    @Test
    public void test_handleProcessingException_MessageNull() throws Exception {
        String message = "{\"result\":[\"\"],\"status\":1,\"statusCode\":\"system_error\"}";
        mockMvc.perform(get("/test/api/serviceErrorWithMessage")).andExpect((result) -> {
            MockHttpServletResponse response = result.getResponse();
            assertEquals(message, response.getContentAsString());
        });
    }

    @Test
    public void test_handleProcessingException_MessageBlank() throws Exception {
        String message = "{\"result\":[\"\"],\"status\":1,\"statusCode\":\"system_error\"}";
        mockMvc.perform(get("/test/api/serviceErrorWithMessage").param("errorMessage", " ")).andExpect((result) -> {
            MockHttpServletResponse response = result.getResponse();
            assertEquals(message, response.getContentAsString());
        });
    }

    @Test
    public void test_handleProcessingException_HaveMessageAndNoOrigin() throws Exception {
        String message = "this is message";
        mockMvc.perform(get("/test/api/serviceErrorWithMessageNoCause").param("errorMessage", message)).andExpect(
                content().string(
                        Is.is("{\"result\":[\"" + message + "\"],\"status\":1,\"statusCode\":\"" + message + "\"}")));
    }

    @Test
    public void test_handleProcessingException_runtimeException() throws Exception {
        String message = "{\"result\":[null],\"status\":1,\"statusCode\":\"system_error\"}";
        mockMvc.perform(get("/test/api/runtimeException")).andExpect(status().isInternalServerError()).andExpect(
                content().string(Is.is(message)));
    }

    @Test
    public void test_badRequest() throws Exception {
        String message = "{\"result\":[null],\"status\":1,\"statusCode\":\"bad_request\"}";
        mockMvc.perform(get("/test/api/illegalArgumentException")).andExpect(status().isBadRequest()).andExpect(
                content().string(Is.is(message)));
    }

    @Test
    public void test_accessDenied() throws Exception {
        String message = "{\"result\":[null],\"status\":1,\"statusCode\":\"access_denied\"}";
        mockMvc.perform(get("/test/api/accessDenied")).andExpect(status().isForbidden())
                .andExpect(content().string(Is.is(message)));
    }
}
