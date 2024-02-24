package tech.corefinance.account.common.model;

import lombok.Data;

@Data
public class AccountFee {
    private boolean activated;
    private String id;
    private String name;
    private AccountFeeType type;
    private Double amount;
    private String currencyId;
    private Double disbursedPercent;

    private MonthlyPayOption monthlyPayOption;
    private Boolean requiredFeeApplication;
}
