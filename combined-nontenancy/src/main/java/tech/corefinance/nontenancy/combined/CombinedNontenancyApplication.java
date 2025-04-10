package tech.corefinance.nontenancy.combined;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringUtils;
import tech.corefinance.common.enums.CommonConstants;

import java.io.File;

@SpringBootApplication(
        scanBasePackages = {"tech.corefinance.userprofile", "tech.corefinance.common", "tech.corefinance.geocode", "tech.corefinance.feign.client"})
@EnableJpaRepositories(
        basePackages = {
                "tech.corefinance.userprofile.repository", "tech.corefinance.common.jpa.repository",
                "tech.corefinance.common.repository", "tech.corefinance.geocode.repository",
                "tech.corefinance.userprofile.common.repository", "tech.corefinance.userprofile.common.entity_author"
        })
@EntityScan(
        basePackages = {"tech.corefinance.geocode.entity", "tech.corefinance.common.jpa.model", "tech.corefinance.common.model", "tech.corefinance.userprofile.entity", "tech.corefinance.userprofile.common.entity",
                "tech.corefinance.common.entity_author"})
@ConditionalOnProperty(prefix = "tech.app.enabled", name = "combined-nontenancy", havingValue = "true", matchIfMissing = true)
@EnableFeignClients(basePackages = {"tech.corefinance.feign.client"})
public class CombinedNontenancyApplication {

    public static void main(String[] args) {
        // Logs
        String logFileNameDefault = "core_finance_combined_nontenancy";
        if (!StringUtils.hasText(System.getProperty(CommonConstants.LOGBACK_FILE_PATH_KEY))) {
            System.setProperty(CommonConstants.LOGBACK_FILE_PATH_KEY, CommonConstants.LOGBACK_FILE_PATH_DEFAULT);
        }

        if (!StringUtils.hasText(System.getProperty(CommonConstants.LOGBACK_FILE_NAME_KEY))) {
            System.setProperty(CommonConstants.LOGBACK_FILE_NAME_KEY, logFileNameDefault);
        }

        SpringApplicationBuilder app = new SpringApplicationBuilder(CombinedNontenancyApplication.class);
        File file = new File(CommonConstants.LOGBACK_FILE_PATH_DEFAULT + "/" + logFileNameDefault + "_shutdown.pid");
        Logger log = LoggerFactory.getLogger(CombinedNontenancyApplication.class);
        log.info("Log folder path [{}]", System.getProperty(CommonConstants.LOGBACK_FILE_PATH_KEY));
        log.info("Generated PID file {}", file.getAbsolutePath());
        log.info("Log file name [{}]", System.getProperty(CommonConstants.LOGBACK_FILE_NAME_KEY));
        app.build().addListeners(new ApplicationPidFileWriter(file));
        app.run(args);
    }

}
