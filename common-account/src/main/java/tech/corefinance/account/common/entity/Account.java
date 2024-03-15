package tech.corefinance.account.common.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import tech.corefinance.account.common.model.AccountFee;
import tech.corefinance.common.dto.BasicUserDto;
import tech.corefinance.common.model.AuditableEntity;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;
import tech.corefinance.product.common.enums.AccountState;

import java.time.ZonedDateTime;
import java.util.List;

@MappedSuperclass
@Data
public class Account implements GenericModel<String>, AuditableEntity<BasicUserDto>, ModifiedDateTrackedEntity<ZonedDateTime> {
    @Id
    private String id;

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

    @NotBlank(message = "account_name_empty")
    private String name;
    @NotBlank(message = "product_category_empty")
    private String category;
    private String type;
    private String description;
    @Enumerated(EnumType.STRING)
    private AccountState status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "account_fees")
    private List<AccountFee> accountFees;

    @Column(name = "supported_currencies")
    private String[] supportedCurrencies;

    @Column(name = "product_id")
    private String productId;
}
