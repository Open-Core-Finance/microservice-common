package tech.corefinance.feign.client.product;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import tech.corefinance.feign.client.GenericClient;
import tech.corefinance.feign.client.GenericFeignClientConfig;
import tech.corefinance.product.common.dto.DepositProductDto;

@FeignClient(name = "product-deposit-product-service", url = "${tech.corefinance.services.url.product}/deposit-products",
    configuration = GenericFeignClientConfig.class)
@ConditionalOnProperty(prefix = "tech.corefinance.services.url", name = "product")
public interface DepositProductClient extends GenericClient<String, DepositProductDto, DepositProductDto> {
}
