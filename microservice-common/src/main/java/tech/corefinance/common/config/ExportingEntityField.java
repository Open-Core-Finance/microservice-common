package tech.corefinance.common.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExportingEntityField {
    private String label;
    private String field;
    /**
     * Supported format:
     * 1. Number format. Example: 0,000.00
     * 2. String format with {} as placeholder for the value. Example: Custom text and val {}
     * 3. Boolean format. Example: True|False or Yes|No or any other like ValT|ValF
     * 4. Date format. Example: dd/MM/yyyy
     */
    private String format;
    private int index;
}
