package tech.corefinance.common.export.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMethod;
import tech.corefinance.common.export.config.ExportingCsvConfig;
import tech.corefinance.common.export.config.ExportingExcelConfig;
import tech.corefinance.common.model.ResourceAction;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
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
        String resourceActionExportConfigFile = "classpath:resource-action-export-csv-config.json";
        ExportingCsvConfig config = exportingService.loadCsvConfigFromPath(resourceActionExportConfigFile);
        // Data
        List<ResourceAction> resourceActions = new LinkedList<>();
        resourceActions.add(new ResourceAction("test", "search", "/search/:id", RequestMethod.POST, "Descriptions", true, "/search/{id}"));
        resourceActions.add(
                new ResourceAction("test", "search2", "/search2/:id", RequestMethod.GET, "Descriptions 2", false, "/search2/{id}"));
        // Call test
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exportingService.exportToUtf8Csv(resourceActions, config, baos, new LinkedList<>());
        String expected = ExportingServiceImpl.UTF8_BOM_UNICODE + new String(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("resource-action-export-test-full-result.csv"))
                        .readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(expected, baos.toString(StandardCharsets.UTF_8));
    }

    @Test
    public void test_exportToExcel_full() throws IOException {
        String resourceActionExportConfigFile = "classpath:resource-action-export-excel-config.json";
        ExportingExcelConfig config = exportingService.loadExcelConfigFromPath(resourceActionExportConfigFile);
        log.error("Config {}", config);
        // Data
        List<ResourceAction> resourceActions = new LinkedList<>();
        resourceActions.add(new ResourceAction("test", "search", "/search/:id", RequestMethod.POST, "Descriptions", true, "/search/{id}"));
        resourceActions.add(
                new ResourceAction("test", "search2", "/search2/:id", RequestMethod.GET, "Descriptions 2", false, "/search2/{id}"));
        // Call test
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        exportingService.exportToExcel(resourceActions, config, baos, new LinkedList<>());
        //        try (FileOutputStream fos = new FileOutputStream("out.xlsx")) {
        //            fos.write(baos.toByteArray());
        //        }
        // Read file to verify content
        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(baos.toByteArray()));
        // First sheet
        Sheet sheet = workbook.getSheetAt(0);
        // Row 1 after header
        var row = sheet.getRow(1);
        assertEquals("Resource search_test_search_id", row.getCell(0).getStringCellValue());
        // Row 2
        row = sheet.getRow(2);
        assertEquals("NO", row.getCell(3).getStringCellValue());
    }
}
