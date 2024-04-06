package tech.corefinance.feign.client.product;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import tech.corefinance.feign.client.GenericClient;
import tech.corefinance.feign.client.GenericFeignClientConfig;
import tech.corefinance.feign.client.product.entity.ProductTypeResponse;

@FeignClient(name = "product-product-type-service", url = "${tech.corefinance.services.url.product}/product-types",
    configuration = GenericFeignClientConfig.class)
@ConditionalOnProperty(prefix = "tech.corefinance.services.url", name = "product")
public interface ProductTypeClient extends GenericClient<String, ProductTypeResponse, ProductTypeResponse> {
}
