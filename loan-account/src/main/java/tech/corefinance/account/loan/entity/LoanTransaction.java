package tech.corefinance.account.loan.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.account.common.entity.AccountTransaction;
import tech.corefinance.account.common.enums.CustomerType;
import tech.corefinance.common.audit.EntityDeleteListener;

@Entity
@Table(name = "loan_transaction")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({EntityDeleteListener.class})
public class LoanTransaction extends AccountTransaction {

    @Column(name = "gl_account_id")
    private String glAccountId;

    @JoinColumn(name = "loan_account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private LoanAccount loanAccount;
    @Column(name = "loan_customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType loanCustomerType;
    @Column(name = "loan_customer_id")
    private long loanCustomerId;
}
