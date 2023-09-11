package tech.corefinance.common.util;

import tech.corefinance.common.repository.ResourceActionRepository;
import tech.corefinance.common.test.support.app.TestCommonApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest(classes = TestCommonApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles({"common", "default", "unittest"})
@ComponentScan(basePackages = {"tech.corefinance.kuniv"})
public class UtilIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtilIntegrationTest.class);

    @Value("${export.format.date}")
    private String exportDateFormat;
    @Value("${export.format.datetime}")
    private String exportDateTimeFormat;
    @Autowired
    private Util util;

    @MockBean
    private ResourceActionRepository resourceActionRepository;

    @BeforeEach
    public void setUp() {
        PowerMockito.when(resourceActionRepository.saveAll(Mockito.any())).thenReturn(new LinkedList<>());
    }

    @Test
    void test_checkAndConvertExportData_happyCase() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat(exportDateFormat);
        Object result = util.checkAndConvertExportData(date);
        Assertions.assertEquals(dateFormat.format(date), result);
    }

    @Test
    void test_checkAndConvertExportData_happyCase2() {
        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(exportDateTimeFormat);
        Object result = util.checkAndConvertExportData(date);
        Assertions.assertEquals(dateFormat.format(date), result);
    }

    @Test
    void test_checkAndConvertExportData_happyCase3() {
        Instant date = Instant.now();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(exportDateTimeFormat).withZone(ZoneId.systemDefault());
        Object result = util.checkAndConvertExportData(date);
        Assertions.assertEquals(dateFormat.format(date), result);
    }

    @Test
    void test_checkAndConvertExportData_nullData() {
        Object result = util.checkAndConvertExportData(null);
        Assertions.assertNull(result);
    }

    @Test
    void test_checkAndConvertExportData_notSupportedObject() {
        Object input = new Object();
        Object result = util.checkAndConvertExportData(input);
        Assertions.assertEquals(input, result);
    }
}
