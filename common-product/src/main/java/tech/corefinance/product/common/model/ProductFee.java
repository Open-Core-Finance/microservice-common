package tech.corefinance.product.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.MonthlyPayOption;
import tech.corefinance.product.common.enums.ProductFeeType;

@Data
public class ProductFee {

    private boolean activated;
    private String id;
    private String name;
    private ProductFeeType type;
    private Double amount;
    private String currencyId;
    private Double disbursedPercent;

    private MonthlyPayOption monthlyPayOption;
    private Boolean requiredFeeApplication;
}
