package tech.corefinance.feign.client.geocode;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import tech.corefinance.feign.client.GenericClient;
import tech.corefinance.feign.client.GenericFeignClientConfig;
import tech.corefinance.feign.client.geocode.entity.CountryReponse;

@FeignClient(name = "geocode-country-client", url = "${tech.corefinance.services.url.geocode}/countries",
    configuration = GenericFeignClientConfig.class)
@ConditionalOnProperty(prefix = "tech.corefinance.services.url", name = "geocode")
public interface CountryClient extends GenericClient<Integer, CountryReponse, CountryReponse> {
}
