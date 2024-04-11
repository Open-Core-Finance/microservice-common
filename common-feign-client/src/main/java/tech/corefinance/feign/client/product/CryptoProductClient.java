package tech.corefinance.feign.client.product;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import tech.corefinance.feign.client.GenericClient;
import tech.corefinance.feign.client.GenericFeignClientConfig;
import tech.corefinance.feign.client.product.entity.CryptoProductResponse;

@FeignClient(name = "product-crypto-product-service", url = "${tech.corefinance.services.url.product}/crypto-products",
    configuration = GenericFeignClientConfig.class)
@ConditionalOnProperty(prefix = "tech.corefinance.services.url", name = "product")
public interface CryptoProductClient extends GenericClient<String, CryptoProductResponse, CryptoProductResponse> {
}
