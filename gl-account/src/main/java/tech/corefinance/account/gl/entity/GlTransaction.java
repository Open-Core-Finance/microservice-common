package tech.corefinance.account.gl.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.account.common.entity.AccountTransaction;
import tech.corefinance.common.audit.EntityDeleteListener;

@Entity
@Table(name = "gl_transaction")
@Data
@EqualsAndHashCode(callSuper = true)
@EntityListeners({EntityDeleteListener.class})
public class GlTransaction extends AccountTransaction {

    @JoinColumn(name = "gl_account_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GlAccount glAccount;

    @Column(name = "from_account_id")
    private String counterAccountId;

    @Column(name = "from_account_type")
    private String counterAccountType;
}
