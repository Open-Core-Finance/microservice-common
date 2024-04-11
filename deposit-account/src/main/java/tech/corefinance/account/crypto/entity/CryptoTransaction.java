package tech.corefinance.account.crypto.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.account.common.entity.AccountTransaction;
import tech.corefinance.account.common.enums.CustomerType;
import tech.corefinance.common.audit.EntityDeleteListener;

@Entity
@Table(name = "crypto_transaction")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({EntityDeleteListener.class})
public class CryptoTransaction extends AccountTransaction {

    @Column(name = "gl_account_id")
    private String glAccountId;

    @JoinColumn(name = "crypto_account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CryptoAccount cryptoAccount;
    @Column(name = "crypto_customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType cryptoCustomerType;
    @Column(name = "crypto_customer_id")
    private long cryptoCustomerId;
}
