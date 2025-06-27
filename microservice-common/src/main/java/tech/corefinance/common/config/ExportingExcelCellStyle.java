package tech.corefinance.common.config;

import lombok.Data;

@Data
public class ExportingExcelCellStyle {
    private String foregroundColorHex;
    private String fontColor;
    private boolean shinkToFit;
    private String fontName;
}
