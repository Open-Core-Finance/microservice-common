package tech.corefinance.account.deposit.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.account.common.dto.TransactionRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class DepositTransactionRequest extends TransactionRequest {

}
