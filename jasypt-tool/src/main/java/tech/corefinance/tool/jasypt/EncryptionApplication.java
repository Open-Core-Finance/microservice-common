package tech.corefinance.tool.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.util.StringUtils;
import tech.corefinance.common.enums.CommonConstants;

import java.io.File;

@SpringBootApplication
public class EncryptionApplication {
    public static void main(String[] args) {
        String logFileNameDefault = "core_finance_jasypt_tool";
        if (!StringUtils.hasText(System.getProperty(CommonConstants.LOGBACK_FILE_PATH_KEY))) {
            System.setProperty(CommonConstants.LOGBACK_FILE_PATH_KEY, CommonConstants.LOGBACK_FILE_PATH_DEFAULT);
        }

        if (!StringUtils.hasText(System.getProperty(CommonConstants.LOGBACK_FILE_NAME_KEY))) {
            System.setProperty(CommonConstants.LOGBACK_FILE_NAME_KEY, logFileNameDefault);
        }

        SpringApplicationBuilder app = new SpringApplicationBuilder(EncryptionApplication.class);
        File file = new File(CommonConstants.LOGBACK_FILE_PATH_DEFAULT + "/" + logFileNameDefault + "_shutdown.pid");
        Logger log = LoggerFactory.getLogger(EncryptionApplication.class);
        log.info("Log folder path [{}]", System.getProperty(CommonConstants.LOGBACK_FILE_PATH_KEY));
        log.info("Generated PID file {}", file.getAbsolutePath());
        log.info("Log file name [{}]", System.getProperty(CommonConstants.LOGBACK_FILE_NAME_KEY));
        app.build().addListeners(new ApplicationPidFileWriter(file));
        var context = app.run(args);

        var encryptor = context.getBean(StringEncryptor.class);
        var textToEncrypt = "postgres";
        var encryptedText = encryptor.encrypt(textToEncrypt);
        var decryptedText = encryptor.decrypt(encryptedText);
        log.error("Encrypted [{}] to [{}]", textToEncrypt, encryptedText);
        log.error("Decrypted [{}] to [{}]", encryptedText, decryptedText);

        context.close();
    }
}
