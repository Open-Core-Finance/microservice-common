package com.finance.core.common.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "kbtg.common.enabled", name = "property-encryption", havingValue = "true",
        matchIfMissing = true)
public class JasyptConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean(name = "jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(
            @Value("${jasypt.encryptor.password}") String encryptPassword,
            @Value("${jasypt.encryptor.algorithm}") String algorithm,
            @Value("${jasypt.encryptor.salt-generator-class-name}") String saltGeneratorClassName,
            @Value("${jasypt.encryptor.key-obtention-iterations}") String keyObtentionIterations,
            @Value("${jasypt.encryptor.pool-size}") String poolSize,
            @Value("${jasypt.encryptor.provider-name}") String providerName,
            @Value("${jasypt.encryptor.string-output-type}") String stringOutputType
    ) {
        logger.debug("Configured values algorithm [{}], saltGeneratorClassName [{}], keyObtentionIterations [{}]," +
                        "poolSize [{}], providerName [{}], stringOutputType [{}], encryptPassword [{}]", algorithm,
                saltGeneratorClassName, keyObtentionIterations, poolSize, providerName, stringOutputType,
                encryptPassword);
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(encryptPassword);
        config.setAlgorithm(algorithm);
        config.setSaltGeneratorClassName(saltGeneratorClassName);
        config.setKeyObtentionIterations(keyObtentionIterations);
        config.setPoolSize(poolSize);
        config.setProviderName(providerName);
        config.setStringOutputType(stringOutputType);
        encryptor.setConfig(config);
        return encryptor;
    }

}