package tech.corefinance.feign.client.product;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import tech.corefinance.feign.client.GenericClient;
import tech.corefinance.feign.client.GenericFeignClientConfig;
import tech.corefinance.product.common.dto.GlProductDto;

@FeignClient(name = "product-gl-product-client", url = "${tech.corefinance.services.url.product}/gl-products",
    configuration = GenericFeignClientConfig.class)
@ConditionalOnProperty(prefix = "tech.corefinance.services.url", name = "product")
public interface GlProductClient extends GenericClient<String, GlProductDto, GlProductDto> {
}
