package tech.corefinance.balance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import tech.corefinance.account.common.model.AccountType;

import java.io.Serializable;

@Data
public class AccountBalanceId implements Serializable {

    @Column(name = "account_type")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "currency")
    private String currency;
}
