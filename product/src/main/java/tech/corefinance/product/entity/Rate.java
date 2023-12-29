package tech.corefinance.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.product.enums.RateType;

import java.time.ZonedDateTime;

@Entity
@Table(name = "rate")
@Data
public class Rate implements GenericModel<String>, CreateUpdateDto<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "valid_from")
    private ZonedDateTime validFrom;
    @Column(name = "rate_value")
    private Double rateValue;
    private String note;

    @Enumerated(EnumType.STRING)
    private RateType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rate_source_id")
    @JsonIgnore
    private RateSource rateSource;
}
