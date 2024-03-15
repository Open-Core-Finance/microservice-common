package tech.corefinance.product.entity;

import jakarta.persistence.*;
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
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;

import java.time.Duration;
import java.time.ZonedDateTime;

@Entity
@Table(name = "exchange_rate")
@Data
@EntityListeners({EntityBasicUserAuditorListener.class, EntityZonedDateTimeAuditListener.class, EntityDeleteListener.class})
public class ExchangeRate implements GenericModel<ExchangeRateId>, CreateUpdateDto<ExchangeRateId>,
        AuditableEntity<BasicUserDto>, ModifiedDateTrackedEntity<ZonedDateTime> {
    /**
     * Currency code to sell/buy.
     */
    @Id
    private ExchangeRateId id;

    @Column(name = "sell_rate")
    private double sellRate;
    @Column(name = "buy_rate")
    private double buyRate;
    @Column(name = "margin")
    private double margin;

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
    Duration.ofDays(5);
}
