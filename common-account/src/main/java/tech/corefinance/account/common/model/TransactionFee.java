package tech.corefinance.account.common.model;

import lombok.Data;

@Data
public class TransactionFee {
    private AccountFeeType type;
    private String currency;
    private double amount;
    private double vat;
    private double total;
    private String feeCode;
    private String feeGroup;
}
