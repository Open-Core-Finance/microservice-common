package tech.corefinance.feign.client.geocode.entity;

import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class StateResponse implements GenericModel<Integer>, ModifiedDateTrackedEntity<ZonedDateTime>, CreateUpdateDto<Integer> {

    private Integer id;
    private String name;

    private String countryCode;
    private int countryId;
    private String fipsCode;

    private String iso2;
    private String type;
    private BigDecimal latitude;
    private BigDecimal longitude;

    private boolean enabled;

    private String wikiDataId;

    private ZonedDateTime createdDate;
    private ZonedDateTime lastModifiedDate;
}
