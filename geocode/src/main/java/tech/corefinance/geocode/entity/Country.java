package tech.corefinance.geocode.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import tech.corefinance.common.audit.EntityZonedDateTimeAuditListener;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "country")
@EntityListeners({EntityZonedDateTimeAuditListener.class})
public class Country implements GenericModel<Integer>, ModifiedDateTrackedEntity<ZonedDateTime>, CreateUpdateDto<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @JdbcTypeCode(SqlTypes.CHAR)
    private String iso3;
    @JdbcTypeCode(SqlTypes.CHAR)
    private String iso2;
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "numeric_code")
    private String numericCode;

    @Column(name = "phone_code")
    private String phoneCode;

    private String capital;

    private String currency;

    @Column(name = "currency_name")
    private String currencyName;
    @Column(name = "currency_symbol")
    private String currencySymbol;
    private String tld;
    @Column(name = "native")
    private String nativePeople;

    @Column(name = "region_name")
    private String regionName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    @JsonIgnore
    private Region region;
    @Column(name = "region_id", insertable = false, updatable = false)
    private Integer regionId;

    @Column(name = "subregion_name")
    private String subregionName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subregion_id")
    @JsonIgnore
    private SubRegion subRegion;
    @Column(name = "subregion_id", insertable = false, updatable = false)
    private Integer subRegionId;

    private String nationality;
    @JdbcTypeCode(SqlTypes.JSON)
    private String timezones;
    @JdbcTypeCode(SqlTypes.JSON)
    private String translations;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String emoji;
    private String emojiu;

    private boolean enabled;
    /**
     * Rapid API GeoDB Cities.
     */
    @Column(name = "wiki_data_id")
    private String wikiDataId;
    @CreatedDate
    @Column(name = "created_date")
    private ZonedDateTime createdDate;
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private ZonedDateTime lastModifiedDate;
}
