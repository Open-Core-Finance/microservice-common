package tech.corefinance.account.common.dto;

import lombok.Data;
import tech.corefinance.account.common.model.TransactionFee;
import tech.corefinance.account.common.model.TransactionSide;
import tech.corefinance.common.model.CreateUpdateDto;

import java.util.LinkedList;
import java.util.List;

@Data
public class TransactionRequest implements CreateUpdateDto<String> {

    private String entityId;
    private double amount;
    private double vat;
    private String currency;
    private String targetCurrency;
    private List<TransactionFee> transactionFees = new LinkedList<>();
    private TransactionSide transactionSide;
    private String glAccountId;
    private String memo;

    private String counterAccountId;
    private String counterAccountType;
    private String transactionType;
    private String transactionCode;

    private String terminalId;
    private String requestAppId;
    private String requestChannelId;
}
