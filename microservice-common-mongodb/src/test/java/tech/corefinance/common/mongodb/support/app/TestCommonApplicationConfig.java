package tech.corefinance.common.mongodb.support.app;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.reverse.TransitionWalker;
import de.flapdoodle.reverse.transitions.Start;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import tech.corefinance.common.context.ApplicationContextHolder;

import java.util.List;

@Configuration
@Slf4j
@EnableMongoRepositories(
        {"tech.corefinance.common.mongodb.support.repository", "tech.corefinance.common.mongodb.repository",
                "tech.corefinance.common.repository"})
@EnableMongoAuditing
public class TestCommonApplicationConfig {

    @Value("${spring.data.mongodb.port}")
    private int mongoPort;

    @Bean
    TransitionWalker.ReachedState<RunningMongodProcess> createMongod() {
        log.info("Starting mongodb with port [{}]", mongoPort);
        return Mongod.builder().net(Start.to(Net.class)
                .initializedWith(Net.defaults().withPort(mongoPort))).build().start(Version.V7_0_0);
    }

    @Bean
    MongoClient mongoClient(TransitionWalker.ReachedState<RunningMongodProcess> mongodReachedState) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyToClusterSettings(builder ->
                        builder.hosts(List.of(new ServerAddress("localhost", mongoPort))))
                .build();
        return MongoClients.create(settings);
    }

    @PreDestroy
    public void preDestroy() {
        var context = ApplicationContextHolder.getInstance().getApplicationContext();
        var mongodReachedState = context.getBean(TransitionWalker.ReachedState.class);
        if (mongodReachedState != null) {
            log.info("Closing memory MongoDB...");
            mongodReachedState.close();
        } else {
            log.error("Memory MongoDB instance not found!!!");
        }
    }
}
