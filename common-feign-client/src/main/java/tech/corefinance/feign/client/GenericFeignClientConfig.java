package tech.corefinance.feign.client;

import org.springframework.context.annotation.Bean;
import tech.corefinance.feign.client.intercept.AuthFeignInterceptor;

public class GenericFeignClientConfig {
    @Bean
    public AuthFeignInterceptor authFeignInterceptor () {
        return new AuthFeignInterceptor();
    }
}
