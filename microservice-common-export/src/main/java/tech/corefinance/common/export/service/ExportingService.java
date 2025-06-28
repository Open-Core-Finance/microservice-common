package tech.corefinance.common.export.service;

import tech.corefinance.common.export.config.ExportingCsvConfig;
import tech.corefinance.common.export.config.ExportingExcelConfig;

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

    <T> void exportToExcel(List<T> entities, ExportingExcelConfig config, OutputStream output,
            List<ExcelCellDataFormatter<T>> customFormatters) throws IOException;
}
