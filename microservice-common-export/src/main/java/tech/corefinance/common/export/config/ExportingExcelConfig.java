package tech.corefinance.common.export.config;

import lombok.Data;

import java.util.List;

@Data
public class ExportingExcelConfig {
    private List<ExportingExcelSheetConfig> sheetConfigs;
}
