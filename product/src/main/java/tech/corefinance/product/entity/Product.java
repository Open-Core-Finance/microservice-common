package tech.corefinance.product.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.product.model.ProductAvailability;
import tech.corefinance.product.model.ProductFee;
import tech.corefinance.product.model.ProductNewAccountSetting;

import java.util.List;

@MappedSuperclass
@Data
public abstract class Product implements GenericModel<String>, CreateUpdateDto<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    @NotNull
    private String category;
    private String type;
    private String description;
    private boolean activated;
    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ProductAvailability> productAvailabilities;
    @NotNull
    @JdbcTypeCode(SqlTypes.JSON)
    private ProductNewAccountSetting newAccountSetting;
    @NotNull
    private String currency;

    // Product Fees
    private boolean allowArbitraryFees;
    private boolean showInactiveFees;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ProductFee> productFees;
}
