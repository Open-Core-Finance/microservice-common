package tech.corefinance.product.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.product.enums.ProductCategoryType;

@Entity
@Table(name = "product_category")
@Data
public class ProductCategory implements GenericModel<String>, CreateUpdateDto<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Enumerated(EnumType.STRING)
    private ProductCategoryType type;
    private String name;
}
