package tech.corefinance.feign.client.product;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import tech.corefinance.feign.client.GenericClient;
import tech.corefinance.feign.client.GenericFeignClientConfig;
import tech.corefinance.feign.client.product.entity.ProductCategoryResponse;

@FeignClient(name = "product-product-category-service", url = "${tech.corefinance.services.url.product}/product-categories",
    configuration = GenericFeignClientConfig.class)
@ConditionalOnProperty(prefix = "tech.corefinance.services.url", name = "product")
public interface ProductCategoryClient extends GenericClient<String, ProductCategoryResponse, ProductCategoryResponse> {
}
