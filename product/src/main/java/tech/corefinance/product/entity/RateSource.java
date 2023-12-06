package tech.corefinance.product.entity;

import jakarta.persistence.*;
import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.product.enums.RateType;

import java.time.ZonedDateTime;

@Entity
@Table(name = "rate_source")
@Data
public class RateSource implements GenericModel<String>, CreateUpdateDto<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String name;
    private String note;
    @Enumerated(EnumType.STRING)
    private RateType type;
}
