package tech.corefinance.account.gl.service;

import tech.corefinance.account.common.service.AccountTransactionService;
import tech.corefinance.account.gl.entity.GlTransaction;
import tech.corefinance.account.gl.repository.GlTransactionRepository;
import tech.corefinance.common.model.CreateUpdateDto;

public interface GlTransactionService extends AccountTransactionService<GlTransaction, GlTransactionRepository> {
    @Override
    default <D extends CreateUpdateDto<String>> GlTransaction copyAdditionalPropertiesFromDtoToEntity(D source,
                                                                                             GlTransaction dest) {
        // Should do nothing because transaction details was build from original service to make it consistent.
        return dest;
    }
}
