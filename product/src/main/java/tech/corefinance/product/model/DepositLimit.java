package tech.corefinance.product.model;

import lombok.Data;
import tech.corefinance.product.enums.DepositLimitType;

@Data
public class DepositLimit {

    private DepositLimitType type;
    private double value;
}
