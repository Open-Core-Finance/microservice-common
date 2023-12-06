package tech.corefinance.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parse and map field from configure path: "tech.corefinance.initial": <br />
 * - name-separator. Default is "_".<br />
 * - version-separator. Default is ".".<br />
 * Example 1 file name: 1.1_data_list.json will resolve "data_list.json" as name and "1.1" as version.<br />
 * - data-regex will be the map with data name and file name regex. For example 1 record {"role": "classpath:initial/*role*.json"}
 */
@Configuration
@ConfigurationProperties(prefix = "tech.corefinance.initial")
@Data
public class InitDataConfigurations {
    private String nameSeparator = "_";
    private String versionSeparator = ".";
    private Map<String, FileNameRegex> dataRegex = new LinkedHashMap<>();

    @Data
    public static class FileNameRegex {
        private String fileNameRegex;
        private String className;
        private boolean replaceIfExisted;
    }
}
