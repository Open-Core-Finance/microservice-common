package tech.corefinance.feign.client.geocode;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import tech.corefinance.feign.client.GenericClient;
import tech.corefinance.feign.client.GenericFeignClientConfig;
import tech.corefinance.feign.client.geocode.entity.CityResponse;

@FeignClient(name = "geocode-city-service", url = "${tech.corefinance.services.url.geocode}/cities",
    configuration = GenericFeignClientConfig.class)
@ConditionalOnProperty(prefix = "tech.corefinance.services.url", name = "geocode")
public interface CityClient extends GenericClient<Integer, CityResponse, CityResponse> {
}
