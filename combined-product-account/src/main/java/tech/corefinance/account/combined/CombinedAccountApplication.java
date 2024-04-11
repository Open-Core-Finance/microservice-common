package tech.corefinance.account.combined;

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

@SpringBootApplication(scanBasePackages = {
        "tech.corefinance.product", "tech.corefinance.account.deposit", "tech.corefinance.account.common",
        "tech.corefinance.account.gl", "tech.corefinance.account.loan", "tech.corefinance.common",
        "tech.corefinance.customer", "tech.corefinance.feign.client", "tech.corefinance.account.crypto"
})
@EnableJpaRepositories(basePackages = {
        "tech.corefinance.product.repository", "tech.corefinance.account.loan.repository", "tech.corefinance.account.gl.repository",
        "tech.corefinance.account.deposit.repository", "tech.corefinance.account.common.repository",
        "tech.corefinance.common.jpa.repository", "tech.corefinance.common.repository",
        "tech.corefinance.customer.repository", "tech.corefinance.account.crypto.repository"
})
@EntityScan(basePackages = {
        "tech.corefinance.product.entity", "tech.corefinance.common.jpa.model", "tech.corefinance.common.model",
        "tech.corefinance.account.deposit.entity", "tech.corefinance.account.common.entity",
        "tech.corefinance.account.loan.entity", "tech.corefinance.account.gl.entity", "tech.corefinance.customer.entity",
        "tech.corefinance.account.crypto.entity"
})
@ConditionalOnProperty(prefix = "tech.app.enabled", name = "combined-product-account", havingValue = "true",matchIfMissing = true)
@EnableFeignClients(basePackages = {"tech.corefinance.feign.client"})
public class CombinedAccountApplication {

    public static void main(String[] args) {
        // Logs
        String logFileNameDefault = "core_finance_combined_product_account";
        if (!StringUtils.hasText(System.getProperty(CommonConstants.LOGBACK_FILE_PATH_KEY))) {
            System.setProperty(CommonConstants.LOGBACK_FILE_PATH_KEY, CommonConstants.LOGBACK_FILE_PATH_DEFAULT);
        }

        if (!StringUtils.hasText(System.getProperty(CommonConstants.LOGBACK_FILE_NAME_KEY))) {
            System.setProperty(CommonConstants.LOGBACK_FILE_NAME_KEY, logFileNameDefault);
        }

        SpringApplicationBuilder app = new SpringApplicationBuilder(CombinedAccountApplication.class);
        File file = new File(CommonConstants.LOGBACK_FILE_PATH_DEFAULT + "/" + logFileNameDefault + "_shutdown.pid");
        Logger log = LoggerFactory.getLogger(CombinedAccountApplication.class);
        log.info("Log folder path [{}]", System.getProperty(CommonConstants.LOGBACK_FILE_PATH_KEY));
        log.info("Generated PID file {}", file.getAbsolutePath());
        log.info("Log file name [{}]", System.getProperty(CommonConstants.LOGBACK_FILE_NAME_KEY));
        app.build().addListeners(new ApplicationPidFileWriter(file));
        app.run(args);
    }

}
