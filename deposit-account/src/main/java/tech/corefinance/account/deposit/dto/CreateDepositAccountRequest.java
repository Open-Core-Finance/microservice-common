package tech.corefinance.account.deposit.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.common.model.CreateUpdateDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateDepositAccountRequest extends GenericCreateDepositAccountRequest implements CreateUpdateDto<String> {
}
