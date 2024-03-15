package tech.corefinance.product.dto;
import lombok.Data;
import tech.corefinance.common.dto.BasicUserDto;

import java.time.ZonedDateTime;

@Data
public class ExchangeRateResponse {

    private String fromCurrency;
    private String toCurrency;
    private double sellRate;
    private double buyRate;
    private double margin;

    private ZonedDateTime createdDate;
    private BasicUserDto createdBy;
    private ZonedDateTime lastModifiedDate;
    private BasicUserDto lastModifiedBy;
}
