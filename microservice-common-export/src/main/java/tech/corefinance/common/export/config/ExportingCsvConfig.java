package tech.corefinance.common.export.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;

import java.util.InvalidPropertiesFormatException;
import java.util.List;

@Data
public class ExportingCsvConfig {
    private boolean header;
    private String delimiter;
    private List<ExportingEntityField> fields;

    @PostConstruct
    public void postConstruct() throws InvalidPropertiesFormatException {
        if (delimiter == null) {
            throw new InvalidPropertiesFormatException("delimiter must not be null!");
        }
        if (delimiter.contains("\"") || delimiter.contains("'")) {
            throw new InvalidPropertiesFormatException("delimiter does not allow quotes!");
        }
    }
}
