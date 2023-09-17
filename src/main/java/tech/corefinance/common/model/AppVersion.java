package tech.corefinance.common.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import tech.corefinance.common.enums.CommonConstants;
import tech.corefinance.common.ex.ServiceProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * Client/Server application version. <br/>
 * <div>Format: major.minor.maintenance-build</div> <div>For example: 2.0.1.123-SNAPSHOT</div>
 */
@Data
@AllArgsConstructor
public class AppVersion {

    /**
     * <div>Major number</div>
     */
    private short major;
    /**
     * <div>Minor number</div>
     */
    private short minor;
    /**
     * <div>Maintenance number</div>
     */
    private short maintenance;
    /**
     * Build value can be null
     */
    private String build;

    public AppVersion() {
        this(CommonConstants.DEFAULT_VERSION_STRING);
    }

    @SneakyThrows(JsonProcessingException.class)
    public AppVersion(String jsonString) {
        if (jsonString.contains(CommonConstants.DEFAULT_VERSION_NUMBER_SEPARATOR)) {
            var versionText = jsonString;
            var buildIndex = versionText.indexOf(CommonConstants.DEFAULT_VERSION_BUILD_SEPARATOR);
            if (buildIndex > 0) {
                build = versionText.substring(buildIndex);
                versionText = versionText.substring(0, buildIndex);
            }
            var arr = versionText.split("\\.");
            major = Short.parseShort(arr[0]);
            minor = Short.parseShort(arr[1]);
            maintenance = Short.parseShort(arr[2]);
        } else {
            AppVersion appVersion = new ObjectMapper().readValue(jsonString, getClass());
            this.major = appVersion.major;
            this.minor = appVersion.getMinor();
            this.maintenance = appVersion.getMaintenance();
            this.build = appVersion.getBuild();
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(major + CommonConstants.DEFAULT_VERSION_NUMBER_SEPARATOR);
        builder = builder.append(minor + CommonConstants.DEFAULT_VERSION_NUMBER_SEPARATOR + maintenance);
        if (StringUtils.hasText(build)) {
            builder.append(CommonConstants.DEFAULT_VERSION_BUILD_SEPARATOR).append(build);
        }
        return builder.toString();
    }
}
