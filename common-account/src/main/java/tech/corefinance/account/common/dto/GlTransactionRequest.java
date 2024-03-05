package tech.corefinance.account.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import tech.corefinance.account.common.entity.AccountTransaction;
import tech.corefinance.common.model.CreateUpdateDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class GlTransactionRequest extends AccountTransaction implements CreateUpdateDto<String> {
}
