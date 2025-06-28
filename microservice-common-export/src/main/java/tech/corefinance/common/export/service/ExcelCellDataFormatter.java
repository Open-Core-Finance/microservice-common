package tech.corefinance.common.export.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import tech.corefinance.common.export.config.ExportingEntityField;
import tech.corefinance.common.export.config.ExportingExcelConfig;

/**
 * Customer data converter to support special field in Excel file.
 *
 * @param <R> Row data type
 */
public interface ExcelCellDataFormatter<R> {
    void transformData(Workbook workbook, Row row, Cell cell, int rowIndex, int columnIndex, Object originalCellData, R rowObject,
            ExportingExcelConfig config, ExportingEntityField fieldConfig);
}
