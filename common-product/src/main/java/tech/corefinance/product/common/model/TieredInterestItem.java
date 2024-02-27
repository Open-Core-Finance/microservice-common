package tech.corefinance.product.common.model;

import lombok.Data;

@Data
public class TieredInterestItem {
    private String currencyId;
    private String currencyName;
    /**
     * Value can be number of days or balance.
     */
    private double fromValue;
    /**
     * Value can be number of days or balance.
     */
    private double toValue;
    /**
     * Percentage value.
     */
    private double interestPercentage;
}
