package tech.corefinance.account.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class AccountBalanceId implements Serializable {
    @Column(name = "account_id")
    private String accountId;
    private String currency;
    @Column(name = "account_type")
    private String accountType;
}
