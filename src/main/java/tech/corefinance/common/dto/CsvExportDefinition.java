package tech.corefinance.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvExportDefinition {
    private String title;
    private String fieldName;
}
