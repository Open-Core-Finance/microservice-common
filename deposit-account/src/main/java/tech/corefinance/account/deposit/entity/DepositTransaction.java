package tech.corefinance.account.deposit.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import tech.corefinance.account.common.entity.AccountTransaction;
import tech.corefinance.account.common.model.TransactionFee;
import tech.corefinance.account.common.model.TransactionSide;
import tech.corefinance.common.audit.EntityDeleteListener;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "deposit_transaction")
@Data
@EntityListeners({EntityDeleteListener.class})
public class DepositTransaction extends AccountTransaction {

    @Column(name = "gl_account_id")
    private String glAccountId;

    @JoinColumn(name = "deposit_account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DepositAccount depositAccount;
}
