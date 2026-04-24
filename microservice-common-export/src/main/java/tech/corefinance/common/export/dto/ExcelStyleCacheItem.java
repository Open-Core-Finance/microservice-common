package tech.corefinance.common.export.dto;

import lombok.Data;
import org.apache.poi.ss.usermodel.CellStyle;
import tech.corefinance.common.export.config.ExportingExcelCellStyle;

import java.util.Objects;

@Data
public class ExcelStyleCacheItem implements Comparable<ExcelStyleCacheItem> {

    private String format;
    private ExportingExcelCellStyle cellStyleConfig;
    private CellStyle style;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExcelStyleCacheItem that = (ExcelStyleCacheItem) o;
        return Objects.equals(format, that.format) && Objects.equals(cellStyleConfig, that.cellStyleConfig) &&
                Objects.equals(style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(format, cellStyleConfig, style);
    }

    @Override
    public int compareTo(ExcelStyleCacheItem o) {
        if (Objects.isNull(o)) {
            return 1;
        }
        int result = 0;
        if (Objects.nonNull(format)) {
            result += format.compareTo(o.format);
        }
        if (cellStyleConfig != null) {
            result += cellStyleConfig.compareTo(o.cellStyleConfig);
        }
        return result;
    }
}
