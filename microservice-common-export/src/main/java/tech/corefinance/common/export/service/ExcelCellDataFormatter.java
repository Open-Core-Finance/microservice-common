package tech.corefinance.common.export.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import tech.corefinance.common.export.config.ExportingEntityField;
import tech.corefinance.common.export.config.ExportingExcelConfig;

public interface ExcelCellDataFormatter {
    <T> void transformData(Workbook workbook, Row row, Cell cell, int rowIndex, int columnIndex, Object originalCellData, T rowObject,
            ExportingExcelConfig config, ExportingEntityField fieldConfig);
}
