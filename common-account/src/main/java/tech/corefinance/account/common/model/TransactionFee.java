package tech.corefinance.account.common.model;

import lombok.Data;
import tech.corefinance.product.common.enums.ProductFeeType;

@Data
public class TransactionFee {
    private ProductFeeType type;
    private String currency;
    private double amount;
    private double vat;
    private double total;
    private String feeCode;
    private String feeGroup;
}
