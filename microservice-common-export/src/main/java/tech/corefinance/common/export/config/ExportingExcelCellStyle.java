package tech.corefinance.common.export.config;

import lombok.Data;

import java.util.Objects;

@Data
public class ExportingExcelCellStyle implements Comparable<ExportingExcelCellStyle> {
    private String foregroundColorHex;
    private String fontColor;
    private boolean shinkToFit;
    private String fontName;

    @Override
    public int compareTo(ExportingExcelCellStyle o) {
        int result = Boolean.compare(shinkToFit, o.shinkToFit);
        if (fontName != null) {
            result += fontName.compareTo(o.fontName);
        }
        if (fontColor != null) {
            result += fontColor.compareTo(o.fontColor);
        }
        if (foregroundColorHex != null) {
            result += foregroundColorHex.compareTo(o.foregroundColorHex);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExportingExcelCellStyle that = (ExportingExcelCellStyle) o;
        return shinkToFit == that.shinkToFit && Objects.equals(foregroundColorHex, that.foregroundColorHex) &&
                Objects.equals(fontColor, that.fontColor) && Objects.equals(fontName, that.fontName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(foregroundColorHex, fontColor, shinkToFit, fontName);
    }
}
