package tech.corefinance.account.common.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import lombok.Data;
import tech.corefinance.common.audit.EntityDeleteListener;
import tech.corefinance.common.model.GenericModel;

@Entity
@Table(name = "account_balance")
@Data
@EntityListeners({EntityDeleteListener.class})
public class AccountBalance implements GenericModel<AccountBalanceId>{

    @EmbeddedId
    private AccountBalanceId id;
    private double amount;
}
