package tech.corefinance.common.export.service;

import tech.corefinance.common.export.config.ExportingCsvConfig;
import tech.corefinance.common.export.config.ExportingEntityField;

public interface CsvCellDataFormatter {
    <T> String transformData(int rowIndex, int columnIndex, Object originalCellData, String printingValue, T rowObject,
            ExportingCsvConfig config, ExportingEntityField fieldConfig);
}
