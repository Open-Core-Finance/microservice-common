package tech.corefinance.common.export.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tech.corefinance.common.export.config.ExportingCsvConfig;
import tech.corefinance.common.export.config.ExportingEntityField;
import tech.corefinance.common.export.config.ExportingExcelCellStyle;
import tech.corefinance.common.export.config.ExportingExcelConfig;
import tech.corefinance.common.export.dto.ExcelSheet;
import tech.corefinance.common.util.CoreFinanceUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ExportingServiceImpl implements ExportingService {
    private static final byte[] UTF8_BOM = {(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    static final String UTF8_BOM_UNICODE = "\uFEFF";
    private final Comparator<ExportingEntityField> exportingFieldComparator;
    private final CoreFinanceUtil coreFinanceUtil;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    public ExportingServiceImpl(CoreFinanceUtil coreFinanceUtil, ResourceLoader resourceLoader, ObjectMapper objectMapper) {
        this.coreFinanceUtil = coreFinanceUtil;
        this.resourceLoader = resourceLoader;
        this.objectMapper = objectMapper;
        exportingFieldComparator = (o1, o2) -> {
            if (o1 == null) {
                return o2 == null ? 0 : -o2.getIndex();
            } else {
                return (o2 == null) ? o1.getIndex() : o1.getIndex() - o2.getIndex();
            }
        };
    }

    public <T> void exportToUtf8Csv(List<T> entities, ExportingCsvConfig config, OutputStream output,
            List<CsvCellDataFormatter<T>> customFormatters) {
        exportToCsvWithEncoding(entities, config, output, StandardCharsets.UTF_8, UTF8_BOM_UNICODE, customFormatters);
    }

    @Override
    public <T> void exportToCsvWithEncoding(List<T> entities, ExportingCsvConfig config, OutputStream output, Charset encoding,
            String bomVal, List<CsvCellDataFormatter<T>> customFormatters) {
        var fields = config.getFields().stream().sorted(exportingFieldComparator).toList();
        OutputStreamWriter osw = new OutputStreamWriter(output, encoding);
        PrintWriter pw = new PrintWriter(new BufferedWriter(osw));
        if (StringUtils.hasText(bomVal)) {
            pw.print(bomVal);
        }
        var rowIndex = -1;
        if (config.isHeader()) {
            rowIndex = 0;
            var columnIndex = 0;
            for (ExportingEntityField entityField : fields) {
                String val = "\"" + entityField.getLabel() + "\"";
                if (columnIndex > 0) {
                    pw.print(config.getDelimiter());
                }
                columnIndex++;
                pw.print(val);
            }
            pw.println();
        }
        // Print contents
        var index = 0;
        for (T entity : entities) {
            if (index > 0) {
                pw.println();
            }
            index++;
            rowIndex++;
            printEntityToCsv(entity, pw, fields, config, rowIndex, customFormatters);
        }
        // Flush data
        pw.flush();
    }

    @Override
    public ExportingCsvConfig loadCsvConfigFromPath(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return objectMapper.readValue(resource.getContentAsString(StandardCharsets.UTF_8), ExportingCsvConfig.class);
    }

    @Override
    public ExportingExcelConfig loadExcelConfigFromPath(String path) throws IOException {
        Resource resource = resourceLoader.getResource(path);
        return objectMapper.readValue(resource.getContentAsString(StandardCharsets.UTF_8), ExportingExcelConfig.class);
    }

    private <T> void printEntityToCsv(T entity, PrintWriter pw, List<ExportingEntityField> fields, ExportingCsvConfig config, int rowIndex,
            List<CsvCellDataFormatter<T>> customFormatters) {
        var index = 0;
        for (ExportingEntityField entityField : fields) {
            Object fieldVal = coreFinanceUtil.getDeepAttributeValue(entity, entityField.getField());
            if (index > 0) {
                pw.print(config.getDelimiter());
            }
            index++;
            if (fieldVal == null) {
                var printVal = customTransformCsvFieldValue(rowIndex, index, null, "NULL", entity, config, entityField, customFormatters);
                pw.print(printVal);
                continue;
            }
            String format = entityField.getFormat();
            if (StringUtils.hasText(format)) {
                var printVal = "\"" + getValueAsFormat(fieldVal, format) + "\"";
                printVal = customTransformCsvFieldValue(rowIndex, index, fieldVal, printVal, entity, config, entityField, customFormatters);
                pw.print(printVal);
            } else {
                var printVal = "\"" + fieldVal + "\"";
                printVal = customTransformCsvFieldValue(rowIndex, index, fieldVal, printVal, entity, config, entityField, customFormatters);
                pw.print(printVal);
            }
        }
    }

    private <T> String getValueAsFormat(@NonNull Object value, @NonNull String format) {
        if (value instanceof String val) {
            return format.replace("{}", val);
        }
        if (value instanceof Number val) {
            DecimalFormat decimalFormat = new DecimalFormat(format);
            return decimalFormat.format(val);
        }
        if (value instanceof Boolean val) {
            var indexOfBool = format.indexOf("|");
            if (indexOfBool >= 0) {
                String trueVal = format.substring(0, indexOfBool);
                String falseVal = format.substring(indexOfBool + 1);
                return val ? trueVal : falseVal;
            }
        }

        if (value instanceof TemporalAccessor val) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
            return dateTimeFormatter.format(val);
        }

        if (value instanceof Date val) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            return dateFormat.format(val);
        }
        log.error("Unsupported format for value {}. Using toString instead", value);
        return value.toString();
    }

    private <T> String customTransformCsvFieldValue(int rowIndex, int columnIndex, Object originalCellData, String printingValue,
            T rowObject, ExportingCsvConfig config, ExportingEntityField fieldConfig, List<CsvCellDataFormatter<T>> customFormatters) {
        for (CsvCellDataFormatter<T> formatter : customFormatters) {
            printingValue = formatter.transformData(rowIndex, columnIndex, originalCellData, printingValue, rowObject, config, fieldConfig);
        }
        return printingValue;
    }

    @Override
    public void exportToExcel(List<? extends ExcelSheet<?>> sheets, OutputStream output,
            List<? extends ExcelCellDataFormatter<?>> customFormatters) throws IOException {
        // 1. Create a new workbook
        try (Workbook workbook = new SXSSFWorkbook()) {
            sheets.sort(Comparator.comparingInt((ExcelSheet<?> a) -> a.getConfig().getSheetIndex()));
            for (ExcelSheet<?> sheetData : sheets) {
                exportSingleExcelSheet(workbook, sheetData, output, customFormatters);
            }
        }
    }

    public void exportSingleExcelSheet(Workbook workbook, ExcelSheet<?> sheetData, OutputStream output,
            List<? extends ExcelCellDataFormatter<?>> customFormatters) throws IOException {
        var config = sheetData.getConfig();
        var entities = sheetData.getData();
        var fields = config.getFields().stream().sorted(exportingFieldComparator).toList();

        // 2. Create a sheet
        Sheet sheet = workbook.createSheet(
                StringUtils.hasText(config.getExportSheetName()) ? config.getExportSheetName() : ("Data_" + System.currentTimeMillis()));

        // Header
        int rowIndex = 0;
        if (config.isHeader()) {
            Row header = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            for (ExportingEntityField entityField : fields) {
                var headerCell = header.createCell(columnIndex++);
                headerCell.setCellValue(entityField.getLabel());
                var headerCellStyleConfig = entityField.getHeaderCellStyle();
                if (headerCellStyleConfig != null) {
                    CellStyle cellStyle = workbook.createCellStyle();
                    applyCellStyle(workbook, headerCell, cellStyle, headerCellStyleConfig);
                    headerCell.setCellStyle(cellStyle);
                }
            }
        }

        // Contents
        for (var entity : entities) {
            log.debug("==================");
            log.debug("Reading data of row [{}]", rowIndex);
            Row dataRow = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            for (ExportingEntityField entityField : fields) {
                log.debug("***Column [{}]", columnIndex);
                Cell cell = dataRow.createCell(columnIndex++);
                Object fieldVal = coreFinanceUtil.getDeepAttributeValue(entity, entityField.getField());
                log.debug("Column value [{}], Column label [{}]", fieldVal, entityField.getLabel());
                var cellStyle = workbook.createCellStyle();
                String format = entityField.getFormat();
                writeValueToExcel(workbook, cell, fieldVal, format, cellStyle);
                var contentCellStyleConfig = entityField.getContentCellStyle();
                if (contentCellStyleConfig != null) {
                    applyCellStyle(workbook, cell, cellStyle, contentCellStyleConfig);
                }
                cell.setCellStyle(cellStyle);
                // Apply custom formatter
                for (ExcelCellDataFormatter<?> dataFormatter : customFormatters) {
                    if (fieldVal == null || dataFormatter.isSupportedType(fieldVal.getClass())) {
                        dataFormatter.transformData(workbook, sheet, dataRow, cell, rowIndex, columnIndex, fieldVal, entity, config,
                                                    entityField);
                    }
                }
            }
        }

        // Write data
        workbook.write(output);

        // Flush to output stream
        output.flush();
    }

    private void writeValueToExcel(Workbook workbook, Cell cell, @Nullable Object value, String format, CellStyle cellStyle) {
        if (value instanceof String val) {
            if (StringUtils.hasText(format)) {
                cell.setCellValue(format.replace("{}", val));
            } else {
                cell.setCellValue(val);
            }
        } else if (value instanceof Boolean val) {
            var indexOfBool = format.indexOf("|");
            if (StringUtils.hasText(format) && indexOfBool >= 0) {
                String trueVal = format.substring(0, indexOfBool);
                String falseVal = format.substring(indexOfBool + 1);
                cell.setCellValue(val ? trueVal : falseVal);
            } else {
                cell.setCellValue(val);
            }
        } else {
            if (StringUtils.hasText(format)) {
                DataFormat dataFormat = workbook.createDataFormat();
                cellStyle.setDataFormat(dataFormat.getFormat(format));
            }
            if (value instanceof Number val) {
                cell.setCellValue(val.doubleValue());
            } else if (value instanceof LocalDateTime val) {
                cell.setCellValue(val);
            } else if (value instanceof LocalDate val) {
                cell.setCellValue(val);
            } else if (value instanceof TemporalAccessor val) {
                // Convert TemporalAccessor to Instant (via LocalDateTime + ZoneId)
                Instant instant = LocalDateTime.from(val).atZone(ZoneId.systemDefault()).toInstant();

                // Convert Instant to Date
                Date date = Date.from(instant);

                // Write to excel
                cell.setCellValue(date);
            } else if (value instanceof Date val) {
                cell.setCellValue(val);
            } else if (value instanceof Calendar val) {
                cell.setCellValue(val);
            } else {
                if (value == null) {
                    cell.setBlank();
                } else {
                    cell.setCellValue(value.toString());
                }
            }
        }
    }

    private void applyCellStyle(Workbook workbook, Cell cell, CellStyle cellStyle, ExportingExcelCellStyle cellStyleConfig) {
        String fontName = cellStyleConfig.getFontName();
        String fontColor = cellStyleConfig.getFontColor();
        if (StringUtils.hasText(fontName) || StringUtils.hasText(fontColor)) {
            var font = (XSSFFont) workbook.createFont();
            cellStyle.setFont(font);
            if (StringUtils.hasText(fontName)) {
                font.setFontName(fontName);
            }
            if (StringUtils.hasText(fontColor)) {
                font.setColor(fromHex(fontColor));
            }
        }
        String foregroundColor = cellStyleConfig.getForegroundColorHex();
        if (StringUtils.hasText(foregroundColor)) {
            cellStyle.setFillForegroundColor(fromHex(foregroundColor));
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        cellStyle.setShrinkToFit(cellStyleConfig.isShinkToFit());
    }

    private XSSFColor fromHex(String colorData) {
        String hex = colorData.replace("#", "");
        byte[] argb = new byte[]{(byte) Integer.parseInt(hex.substring(0, 2), 16), // alpha
                (byte) Integer.parseInt(hex.substring(2, 4), 16), // red
                (byte) Integer.parseInt(hex.substring(4, 6), 16), // green
                (byte) Integer.parseInt(hex.substring(6, 8), 16)  // blue
        };
        return new XSSFColor(argb, null);
    }
}
