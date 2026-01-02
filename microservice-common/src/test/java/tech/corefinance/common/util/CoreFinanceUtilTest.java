package tech.corefinance.common.util;

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
    void test_writeValueToJson_StackOverflowError() {
        var jsonMapper = PowerMockito.mock(JsonMapper.class);
        PowerMockito.when(jsonMapper.writeValueAsString(Mockito.anyString())).thenThrow(new StackOverflowError());
        Assertions.assertEquals("Parsing json failure",
                coreFinanceUtil.writeValueToJson(jsonMapper, "{\"a\": \"b\"}"));
    }
}
