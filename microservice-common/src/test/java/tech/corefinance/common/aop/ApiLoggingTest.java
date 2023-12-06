package tech.corefinance.common.aop;

import tech.corefinance.common.test.support.aop.AopTestController;
import tech.corefinance.common.test.support.app.TestCommonApplication;
import tech.corefinance.common.repository.ResourceActionRepository;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = TestCommonApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles({"common", "default", "unittest"})
public class ApiLoggingTest {

    @Autowired
    private AopTestController aopTestController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceActionRepository resourceActionRepository;

    @BeforeEach
    public void setUp() {
        PowerMockito.when(resourceActionRepository.saveAll(Mockito.any())).thenReturn(new LinkedList<>());
    }

    @Test
    void test_RequestIndex_AopCalled() throws Exception {
        String requestUrl = "/";
        mockMvc.perform(get(requestUrl)).andDo((MvcResult result) ->
                assertEquals(aopTestController.index(), result.getResponse().getContentAsString()));
    }

    @Test
    void test_RequestIndex_AopCalled_ParserConfigurationException() throws Exception {
        String requestUrl = "/parserConfigurationException";
        mockMvc.perform(get(requestUrl)).andExpect(jsonPath("$.statusCode", Is.is("invalid_input_or_response")));
    }

    @Test
    void test_RequestIndex_AopCalled_ThrowableThrown() throws Exception {
        String requestUrl = "/throwable";
        mockMvc.perform(get(requestUrl)).andDo((MvcResult result) ->
            assertTrue(result.getResponse().getContentAsString().contains("\"statusCode\":\"system_error\""))
        );
    }

    @Test
    void test_RequestIndex_AopCalled_voidReturned() throws Exception {
        String requestUrl = "/void";
        mockMvc.perform(get(requestUrl)).andDo((MvcResult result) ->
            assertEquals(HttpStatus.MOVED_PERMANENTLY.name(), result.getResponse().getContentAsString())
        );
    }
}
