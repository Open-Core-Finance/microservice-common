package tech.corefinance.account.crypto.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.account.deposit.dto.GenericCreateDepositAccountRequest;
import tech.corefinance.common.model.CreateUpdateDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateCryptoAccountRequest extends GenericCreateDepositAccountRequest implements CreateUpdateDto<String> {
}
