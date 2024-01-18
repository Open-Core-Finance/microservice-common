package tech.corefinance.product.model;

import lombok.Data;

@Data
public class CurrencyLimitValue {
    private String currencyId;
    private double limit;
}
