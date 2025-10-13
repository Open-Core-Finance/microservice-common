package tech.corefinance.common.export.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import tech.corefinance.common.export.config.ExportingEntityField;
import tech.corefinance.common.export.config.ExportingExcelSheetConfig;

/**
 * Customer data converter to support special field in Excel file.
 *
 */
@FunctionalInterface
public interface ExcelCellDataFormatter {
    void transformData(Workbook workbook, Sheet sheet, Row row, Cell cell, int rowIndex, int columnIndex, Object originalCellData,
            Object rowObject, ExportingExcelSheetConfig config, ExportingEntityField fieldConfig);

    /**
     * Check if this formatter support specific type of data or not.
     *
     * @param entityClass Data type
     * @return True if supported
     */
    default boolean isSupportedEntity(Class<?> entityClass) {
        return true;
    }
}
