package tech.corefinance.common.mongodb.support.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"tech.corefinance.common"})
public class TestCommonApplication {

}
