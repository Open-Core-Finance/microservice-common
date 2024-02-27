package tech.corefinance.product.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.DepositLimitType;

@Data
public class DepositLimit {

    private String currencyId;
    private String currencyName;
    private DepositLimitType type;
    private double value;
}
