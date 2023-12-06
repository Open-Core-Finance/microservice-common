package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.MonthlyPayOption;
import tech.corefinance.product.enums.ProductFeeType;

@Data
public class ProductFee {

    private boolean activated;
    private String id;
    private String name;
    private ProductFeeType type;
    private Double amount;
    private Double disbursedPercent;

    private MonthlyPayOption monthlyPayOption;
    private Boolean requiredFeeApplication;
}
