package tech.corefinance.common.export.service;

import org.apache.poi.ss.usermodel.Workbook;
import tech.corefinance.common.export.config.ExportingCsvConfig;
import tech.corefinance.common.export.config.ExportingExcelConfig;
import tech.corefinance.common.export.dto.ExcelSheet;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

public interface ExportingService {
    <T> void exportToUtf8Csv(List<T> entities, ExportingCsvConfig config, OutputStream output,
            List<CsvCellDataFormatter<T>> customFormatters);

    <T> void exportToCsvWithEncoding(List<T> entities, ExportingCsvConfig config, OutputStream output, Charset encoding, String bomVal,
            List<CsvCellDataFormatter<T>> customFormatters);

    ExportingCsvConfig loadCsvConfigFromPath(String path) throws IOException;

    ExportingExcelConfig loadExcelConfigFromPath(String path) throws IOException;

    void exportToExcel(List<? extends ExcelSheet<?>> sheets, OutputStream output, List<? extends ExcelCellDataFormatter> customFormatters)
            throws IOException;

    void exportSingleExcelSheet(Workbook workbook, ExcelSheet<?> sheetData, OutputStream output,
            List<? extends ExcelCellDataFormatter> customFormatters) throws IOException;
}
