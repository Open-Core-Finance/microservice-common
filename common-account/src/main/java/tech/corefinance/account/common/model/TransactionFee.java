package tech.corefinance.account.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.DepositProductFeeType;

@Data
public class TransactionFee {
    private DepositProductFeeType type;
    private String currency;
    private double amount;
    private double vat;
    private double total;
    private String feeCode;
    private String feeGroup;
}
