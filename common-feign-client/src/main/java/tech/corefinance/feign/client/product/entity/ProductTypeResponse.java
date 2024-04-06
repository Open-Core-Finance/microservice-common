package tech.corefinance.feign.client.product.entity;

import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;

@Data
public class ProductTypeResponse implements GenericModel<String>, CreateUpdateDto<String> {
    private String id;
    private ProductCategoryType type;
    private String name;
}
