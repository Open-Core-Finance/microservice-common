package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.DepositLimitType;

@Data
public class DepositLimit {

    private String currencyId;
    private String currencyName;
    private DepositLimitType type;
    private double value;
}
