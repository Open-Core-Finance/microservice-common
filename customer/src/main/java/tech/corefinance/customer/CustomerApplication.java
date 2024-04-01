package tech.corefinance.customer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.util.StringUtils;
import tech.corefinance.common.enums.CommonConstants;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.File;

@SpringBootApplication(scanBasePackages = {"tech.corefinance.customer", "tech.corefinance.common", "tech.corefinance.feign.client"})
@EnableJpaRepositories(basePackages = {"tech.corefinance.customer.repository", "tech.corefinance.common.jpa.repository", "tech.corefinance.common.repository"})
@EntityScan(basePackages = {"tech.corefinance.common.jpa.model", "tech.corefinance.customer.entity", "tech.corefinance.common.model"})
@ConditionalOnProperty(prefix = "tech.app.enabled", name = "customer", havingValue = "true",matchIfMissing = true)
@EnableFeignClients(basePackages = {"tech.corefinance.feign.client.geocode"})
public class CustomerApplication {

    public static void main(String[] args) {
        String logFileNameDefault = "core_finance_customer";
        if (!StringUtils.hasText(System.getProperty(CommonConstants.LOGBACK_FILE_PATH_KEY))) {
            System.setProperty(CommonConstants.LOGBACK_FILE_PATH_KEY, CommonConstants.LOGBACK_FILE_PATH_DEFAULT);
        }

        if (!StringUtils.hasText(System.getProperty(CommonConstants.LOGBACK_FILE_NAME_KEY))) {
            System.setProperty(CommonConstants.LOGBACK_FILE_NAME_KEY, logFileNameDefault);
        }

        SpringApplicationBuilder app = new SpringApplicationBuilder(CustomerApplication.class);
        File file = new File(CommonConstants.LOGBACK_FILE_PATH_DEFAULT + "/" + logFileNameDefault + "_shutdown.pid");
        Logger log = LoggerFactory.getLogger(CustomerApplication.class);
        log.info("Log folder path [{}]", System.getProperty(CommonConstants.LOGBACK_FILE_PATH_KEY));
        log.info("Generated PID file {}", file.getAbsolutePath());
        log.info("Log file name [{}]", System.getProperty(CommonConstants.LOGBACK_FILE_NAME_KEY));
        app.build().addListeners(new ApplicationPidFileWriter(file));
        app.run(args);
    }

}
