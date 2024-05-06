package tech.corefinance.account.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tech.corefinance.account.kafka")
@Data
public class AccountKafkaConfig {
    private String balancesInitTopic;
    private String balancesCleanupTopic;
}
