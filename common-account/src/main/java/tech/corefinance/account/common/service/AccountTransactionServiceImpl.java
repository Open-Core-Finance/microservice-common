package tech.corefinance.account.common.service;

import tech.corefinance.account.common.dto.TransactionRequest;
import tech.corefinance.account.common.entity.AccountTransaction;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.repository.CommonResourceRepository;

import java.util.LinkedList;

public abstract class AccountTransactionServiceImpl<T extends AccountTransaction, R extends CommonResourceRepository<T, String>>
        implements AccountTransactionService<T, R> {
    @Override
    public <D extends CreateUpdateDto<String>> void copyAdditionalPropertiesFromDtoToEntity(D source, T dest) {
        AccountTransactionService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        if (source instanceof TransactionRequest tranRequest) {
            // TODO
            var fees = tranRequest.getTransactionFees();
            if (fees != null) {
                for (var fee : fees) {
                    fee.setTotal(fee.getAmount() + fee.getVat());
                }
            } else {
                dest.setTransactionFees(new LinkedList<>());
            }
        }
    }
}
