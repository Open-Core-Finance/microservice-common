package tech.corefinance.common.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import tech.corefinance.common.ex.ServiceProcessingException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
@AllArgsConstructor
public class AppVersion {

    /**
     * <div>Format: major.minor.maintenance.build</div> <div>For example: 2.0.1.123-SNAPSHOT</div>
     */
    private short major;
    private short minor;
    private short maintenance;
    private String build;

    public AppVersion() {
        this.build = "ANY";
    }

    public AppVersion(String jsonString) {
        try {
            AppVersion appVersion = new ObjectMapper().readValue(jsonString, getClass());
            this.major = appVersion.major;
            this.minor = appVersion.getMinor();
            this.maintenance = appVersion.getMaintenance();
            this.build = appVersion.getBuild();
        } catch (JsonProcessingException e) {
            throw new ServiceProcessingException("Parse version error", e);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(major + ".");
        builder = builder.append(minor + "." + maintenance);
        if (StringUtils.hasText(build)) {
            builder.append("-").append(build);
        }
        return builder.toString();
    }
}
