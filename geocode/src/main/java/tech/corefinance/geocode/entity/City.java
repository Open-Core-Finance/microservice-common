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
@Table(name = "city")
@EntityListeners({EntityZonedDateTimeAuditListener.class})
public class City implements GenericModel<Integer>, ModifiedDateTrackedEntity<ZonedDateTime>, CreateUpdateDto<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "state_id")
    private State state;
    @Column(name = "state_id", insertable = false, updatable = false)
    private int stateId;
    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "country_code")
    @JdbcTypeCode(SqlTypes.CHAR)
    private String countryCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    @JsonIgnore
    private Country country;
    @Column(name = "country_id", insertable = false, updatable = false)
    private int countryId;

    private BigDecimal latitude;
    private BigDecimal longitude;

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
