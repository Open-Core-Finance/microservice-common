package tech.corefinance.common.export.dto;

import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import tech.corefinance.common.export.config.ExportingExcelCellStyle;

import java.util.HashSet;
import java.util.Set;

@Data
public class ExcelStyleCache {
    private Set<ExcelStyleCacheItem> items = new HashSet<>();

    public CellStyle getCellStyleByFormat(Workbook workbook, Cell cell, String format) {
        for (ExcelStyleCacheItem item : items) {
            var f = item.getFormat();
            if (f != null && f.equals(format)) {
                return item.getStyle();
            }
        }
        return null;
    }

    public CellStyle getCellStyleByConfig(Workbook workbook, Cell cell, ExportingExcelCellStyle cellStyleConfig) {
        for (ExcelStyleCacheItem item : items) {
            var config = item.getCellStyleConfig();
            if (config != null && config.equals(cellStyleConfig)) {
                return item.getStyle();
            }
        }
        return null;
    }

    public void addItemByConfig(ExportingExcelCellStyle cellStyleConfig, CellStyle cellStyle) {
        ExcelStyleCacheItem item = new ExcelStyleCacheItem();
        item.setCellStyleConfig(cellStyleConfig);
        item.setStyle(cellStyle);
        items.add(item);
    }

    public void addItemByFormat(String format, CellStyle cellStyle) {
        ExcelStyleCacheItem item = new ExcelStyleCacheItem();
        item.setFormat(format);
        item.setStyle(cellStyle);
        items.add(item);
    }
}
