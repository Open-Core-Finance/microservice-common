package tech.corefinance.common.service;

import tech.corefinance.common.config.ExportingCsvConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

public interface ExportingService {
    <T> void exportToUtf8Csv(List<T> entities, ExportingCsvConfig config, OutputStream output);

    <T> void exportToCsvWithEncoding(List<T> entities, ExportingCsvConfig config, OutputStream output, Charset encoding, String bomVal);

    ExportingCsvConfig loadConfigFromPath(String path) throws IOException;
}
