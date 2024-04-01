package tech.corefinance.feign.client.geocode.entity;

import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class CityResponse implements GenericModel<Integer>, ModifiedDateTrackedEntity<ZonedDateTime>, CreateUpdateDto<Integer> {

    private Integer id;
    private String name;


    private int stateId;
    private String stateCode;

    private String countryCode;
    private int countryId;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private boolean enabled;
    /**
     * Rapid API GeoDB Cities.
     */
    private String wikiDataId;
    private ZonedDateTime createdDate;
    private ZonedDateTime lastModifiedDate;
}
