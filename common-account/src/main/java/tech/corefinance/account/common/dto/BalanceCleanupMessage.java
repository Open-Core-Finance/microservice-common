package tech.corefinance.account.common.dto;

import lombok.Data;
import tech.corefinance.account.common.model.AccountType;

@Data
public class BalanceCleanupMessage {
    private String accountId;
    private AccountType accountType;
}
