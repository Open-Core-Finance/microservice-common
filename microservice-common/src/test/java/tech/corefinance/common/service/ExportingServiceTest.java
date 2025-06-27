package tech.corefinance.common.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.config.ExportingCsvConfig;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExportingServiceTest {
    private ExportingService exportingService;

    @BeforeEach
    public void setUp() {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        CoreFinanceUtil coreFinanceUtil = new CoreFinanceUtil();
        ObjectMapper objectMapper = new ObjectMapper();
        exportingService = new ExportingServiceImpl(coreFinanceUtil, resourceLoader, objectMapper);
    }

    @Test
    public void test_exportToUtf8Csv_full() throws IOException {
        String resourceActionExportConfigFile = "classpath:resource-action-export-config.json";
        ExportingCsvConfig config = exportingService.loadConfigFromPath(resourceActionExportConfigFile);
        // Data
        List<ResourceAction> resourceActions = new LinkedList<>();
        resourceActions.add(new ResourceAction("test", "search", "/search/:id", RequestMethod.POST, "Descriptions", true, "/search/{id}"));
        resourceActions.add(
                new ResourceAction("test", "search2", "/search2/:id", RequestMethod.GET, "Descriptions 2", false, "/search2/{id}"));
        // Call test
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exportingService.exportToUtf8Csv(resourceActions, config, baos);
        String expected = ExportingServiceImpl.UTF8_BOM_UNICODE + new String(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("resource-action-export-test-full-result.csv"))
                        .readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(expected, baos.toString(StandardCharsets.UTF_8));
    }
}
