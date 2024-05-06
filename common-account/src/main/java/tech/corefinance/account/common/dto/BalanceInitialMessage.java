package tech.corefinance.account.common.dto;

import lombok.Data;
import tech.corefinance.account.common.model.AccountType;

@Data
public class BalanceInitialMessage {
    private String accountId;
    private AccountType accountType;
    private String[] supportedCurrencies;
}
