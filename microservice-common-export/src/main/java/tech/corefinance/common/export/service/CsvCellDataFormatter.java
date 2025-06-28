package tech.corefinance.common.export.service;

import tech.corefinance.common.export.config.ExportingCsvConfig;
import tech.corefinance.common.export.config.ExportingEntityField;

/**
 * Customer data converter to support special field in CSV file.
 *
 * @param <R> Row data type
 */
public interface CsvCellDataFormatter<R> {
    String transformData(int rowIndex, int columnIndex, Object originalCellData, String printingValue, R rowObject,
            ExportingCsvConfig config, ExportingEntityField fieldConfig);
}
