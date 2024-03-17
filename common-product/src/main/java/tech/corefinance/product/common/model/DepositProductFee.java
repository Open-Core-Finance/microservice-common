package tech.corefinance.product.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.MonthlyPayOption;
import tech.corefinance.product.common.enums.DepositProductFeeType;

@Data
public class DepositProductFee {

    private boolean activated;
    private String id;
    private String name;
    private DepositProductFeeType type;
    private Double amount;
    private String currencyId;

    private MonthlyPayOption monthlyPayOption;
}
