package tech.corefinance.product.dto;

import lombok.Data;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.product.enums.RateType;

import java.time.ZonedDateTime;

@Data
public class RateResponse implements CreateUpdateDto<String>, GenericModel<String> {
    private String id;
    private ZonedDateTime validFrom;
    private Double rateValue;
    private String note;
    private RateType type;
    private String rateSourceId;
    private String rateSourceName;
}
