package tech.corefinance.feign.client.product.entity;

import lombok.Data;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.model.AuditableEntity;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;
import tech.corefinance.product.common.model.ProductAvailability;
import tech.corefinance.product.common.model.ProductNewAccountSetting;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public abstract class Product implements GenericModel<String>, AuditableEntity<BasicUserDto>,
        ModifiedDateTrackedEntity<ZonedDateTime>, CreateUpdateDto<String> {

    private String id;
    private String name;
    private String category;
    private String type;
    private String description;
    private boolean activated;

    private List<ProductAvailability> productAvailabilities;
    private ProductNewAccountSetting newAccountSetting;
    private String[] currencies;

    // Product Fees
    private boolean allowArbitraryFees;
    private boolean showInactiveFees;

    private ZonedDateTime createdDate;
    private BasicUserDto createdBy;
    private ZonedDateTime lastModifiedDate;
    private BasicUserDto lastModifiedBy;
}
