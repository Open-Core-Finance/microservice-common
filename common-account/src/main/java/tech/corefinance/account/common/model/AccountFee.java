package tech.corefinance.account.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.ProductFeeType;

@Data
public class AccountFee {
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
