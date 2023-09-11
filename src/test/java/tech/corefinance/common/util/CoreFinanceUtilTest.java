package tech.corefinance.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

class CoreFinanceUtilTest {

    private CoreFinanceUtil coreFinanceUtil;

    @BeforeEach
    void setUp() {
        coreFinanceUtil = new CoreFinanceUtil();
    }

    @Test
    void test_writeValueToJson_StackOverflowError() throws JsonProcessingException {
        var objectMapper = PowerMockito.mock(ObjectMapper.class);
        PowerMockito.when(objectMapper.writeValueAsString(Mockito.anyString())).thenThrow(new StackOverflowError());
        Assertions.assertEquals("Parsing json failure",
                coreFinanceUtil.writeValueToJson(objectMapper, "{\"a\": \"b\"}"));
    }

    @Test
    void test_writeValueToJson_JsonProcessingException() throws JsonProcessingException {
        var objectMapper = PowerMockito.mock(ObjectMapper.class);
        PowerMockito.when(objectMapper.writeValueAsString(Mockito.anyString())).thenThrow(
                ValueInstantiationException.from(null, null,  (JavaType) null));
        Assertions.assertEquals("Parsing json failure",
                coreFinanceUtil.writeValueToJson(objectMapper, "{\"a\": \"b\"}"));
    }
}
