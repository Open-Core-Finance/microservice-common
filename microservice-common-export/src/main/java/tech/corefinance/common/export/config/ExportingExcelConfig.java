package tech.corefinance.common.export.config;

import lombok.Data;

import java.util.List;

@Data
public class ExportingExcelConfig {
    private boolean header;
    private String exportSheetName;
    private List<ExportingEntityField> fields;
}
