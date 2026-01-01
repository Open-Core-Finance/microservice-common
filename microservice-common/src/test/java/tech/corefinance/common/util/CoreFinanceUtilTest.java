package tech.corefinance.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import tools.jackson.databind.json.JsonMapper;

class CoreFinanceUtilTest {

    private CoreFinanceUtil coreFinanceUtil;

    @BeforeEach
    void setUp() {
        coreFinanceUtil = new CoreFinanceUtil();
    }

    @Test
    void test_writeValueToJson_StackOverflowError() throws JsonProcessingException {
        var objectMapper = PowerMockito.mock(JsonMapper.class);
        PowerMockito.when(objectMapper.writeValueAsString(Mockito.anyString())).thenThrow(new StackOverflowError());
        Assertions.assertEquals("Parsing json failure",
                coreFinanceUtil.writeValueToJson(objectMapper, "{\"a\": \"b\"}"));
    }
}
