package tech.corefinance.common.export.dto;

import lombok.Data;
import tech.corefinance.common.export.config.ExportingExcelSheetConfig;

import java.util.List;

@Data
public class ExcelSheet<T> {
    private ExportingExcelSheetConfig config;
    private List<T> data;
}
