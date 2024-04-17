package tech.corefinance.feign.client.geocode;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import tech.corefinance.feign.client.GenericClient;
import tech.corefinance.feign.client.GenericFeignClientConfig;
import tech.corefinance.feign.client.geocode.entity.StateResponse;

@FeignClient(name = "geocode-state-client", url = "${tech.corefinance.services.url.geocode}/states",
    configuration = GenericFeignClientConfig.class)
@ConditionalOnProperty(prefix = "tech.corefinance.services.url", name = "geocode")
public interface StateClient extends GenericClient<Integer, StateResponse, StateResponse> {
}
