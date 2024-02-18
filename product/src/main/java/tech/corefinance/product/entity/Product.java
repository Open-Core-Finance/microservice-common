package tech.corefinance.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import tech.corefinance.common.audit.EntityBasicUserAuditorListener;
import tech.corefinance.common.audit.EntityDeleteListener;
import tech.corefinance.common.audit.EntityZonedDateTimeAuditListener;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.model.AuditableEntity;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;
import tech.corefinance.product.model.ProductAvailability;
import tech.corefinance.product.model.ProductFee;
import tech.corefinance.product.model.ProductNewAccountSetting;

import java.time.ZonedDateTime;
import java.util.List;

@MappedSuperclass
@Data
@EntityListeners({EntityBasicUserAuditorListener.class, EntityZonedDateTimeAuditListener.class, EntityDeleteListener.class})
public abstract class Product implements GenericModel<String>, AuditableEntity<BasicUserDto>,
        ModifiedDateTrackedEntity<ZonedDateTime> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @NotBlank(message = "product_name_empty")
    private String name;
    @NotBlank(message = "product_category_empty")
    private String category;
    private String type;
    private String description;
    private boolean activated;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "product_availabilities")
    private List<ProductAvailability> productAvailabilities;
    @NotNull(message = "new_account_type_empty")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "new_account_setting")
    private ProductNewAccountSetting newAccountSetting;
    @NotNull(message = "currencies_empty")
    private String[] currencies;

    // Product Fees
    @Column(name = "allow_arbitrary_fees")
    private boolean allowArbitraryFees;
    @Column(name = "show_inactive_fees")
    private boolean showInactiveFees;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "product_fees")
    private List<ProductFee> productFees;

    @CreatedDate
    @Column(name = "created_date")
    private ZonedDateTime createdDate;
    @CreatedBy
    @Column(name = "created_by")
    @JdbcTypeCode(SqlTypes.JSON)
    private BasicUserDto createdBy;
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private ZonedDateTime lastModifiedDate;
    @CreatedBy
    @Column(name = "last_modified_by")
    @JdbcTypeCode(SqlTypes.JSON)
    private BasicUserDto lastModifiedBy;
}
