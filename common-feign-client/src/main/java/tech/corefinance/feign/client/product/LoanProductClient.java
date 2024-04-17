package tech.corefinance.feign.client.product;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import tech.corefinance.feign.client.GenericClient;
import tech.corefinance.feign.client.GenericFeignClientConfig;
import tech.corefinance.product.common.dto.LoanProductDto;

@FeignClient(name = "product-loan-product-client", url = "${tech.corefinance.services.url.product}/loan-products",
    configuration = GenericFeignClientConfig.class)
@ConditionalOnProperty(prefix = "tech.corefinance.services.url", name = "product")
public interface LoanProductClient extends GenericClient<String, LoanProductDto, LoanProductDto> {
}
