package tech.corefinance.account.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import tech.corefinance.account.common.enums.CustomerType;
import tech.corefinance.account.common.model.TransactionFee;
import tech.corefinance.account.common.model.TransactionSide;
import tech.corefinance.common.model.GenericModel;
import tech.corefinance.common.model.ModifiedDateTrackedEntity;

import java.time.ZonedDateTime;
import java.util.List;

@MappedSuperclass
@Data
public class AccountTransaction implements GenericModel<String>, ModifiedDateTrackedEntity<ZonedDateTime> {

    @Id
    private String id;

    @Column(name = "trans_date")
    private ZonedDateTime transDate;

    @CreatedDate
    @Column(name = "created_date")
    private ZonedDateTime createdDate;
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private ZonedDateTime lastModifiedDate;

    /**
     * Transaction amount without fees.
     */
    private double amount;
    /**
     * Transaction VAT.
     */
    private double vat;
    /**
     * Transaction amount which included fees.
     */
    @Column(name = "total_amount")
    private double totalAmount;

    private String currency;
    @Column(name = "target_currency")
    private String targetCurrency;

    @Column(name = "transaction_fees")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<TransactionFee> transactionFees;
    /**
     * Total fee amount.
     */
    @Column(name = "total_fee_amount")
    private double totalFeeAmount;
    /**
     * Total VAT amount of fees.
     */
    @Column(name = "total_fee_vat_amount")
    private double totalFeeVatAmount;
    /**
     * Total VAT amount of fees + Total fee amount
     */
    @Column(name = "total_fee_vat_and_fee_amount")
    private double totalFeeVatAndFeeAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_side")
    private TransactionSide transactionSide;

    @Column(name = "before_tran_amount")
    private double beforeTranAmount;

    @Column(name = "after_tran_amount")
    private double afterTranAmount;

    @Column(name = "applied_exchange_rate")
    private double appliedExchangeRate;

    private String memo;

    @Column(name = "counter_account_id")
    private String counterAccountId;

    @Column(name = "counter_customer_type")
    @Enumerated(EnumType.STRING)
    private CustomerType counterCustomerType;
    @Column(name = "counter_customer_id")
    private long counterCustomerId;

    @Column(name = "counter_account_type")
    private String counterAccountType;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "transaction_code")
    private String transactionCode;

    @Column(name = "terminal_id")
    private String terminalId;

    @Column(name = "request_app_id")
    private String requestAppId;

    @Column(name = "request_channel_id")
    private String requestChannelId;
}
