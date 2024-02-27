package tech.corefinance.product.common.model;

import lombok.Data;

@Data
public class ValueConstraint {
    private String currencyId;
    private String currencyName;
    /**
     * Min amount of money.
     */
    private Double minVal;
    /**
     * Max amount of money.
     */
    private Double maxVal;
    /**
     * Default amount of money.
     */
    private Double defaultVal;
}
