package tech.corefinance.feign.client.geocode.entity;

import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
public class CountryReponse implements GenericModel<Integer>, ModifiedDateTrackedEntity<ZonedDateTime>, CreateUpdateDto<Integer> {

    private Integer id;
    private String name;
    private String iso3;
    private String iso2;
    private String numericCode;
    private String phoneCode;

    private String capital;

    private String currency;

    private String currencyName;
    private String currencySymbol;
    private String tld;
    private String nativePeople;

    private String regionName;
    private Integer regionId;

    private String subregionName;
    private Integer subRegionId;

    private String nationality;
    private String timezones;
    private String translations;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String emoji;
    private String emojiu;

    private boolean enabled;
    /**
     * Rapid API GeoDB Cities.
     */
    private String wikiDataId;
    private ZonedDateTime createdDate;
    private ZonedDateTime lastModifiedDate;
}
