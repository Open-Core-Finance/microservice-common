package tech.corefinance.account.gl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.account.common.dto.TransactionRequest;
import tech.corefinance.account.common.service.AccountTransactionServiceImpl;
import tech.corefinance.account.gl.entity.GlTransaction;
import tech.corefinance.account.gl.repository.GlTransactionRepository;
import tech.corefinance.common.model.CreateUpdateDto;

import java.util.LinkedList;

@Service
@Transactional
public class GlTransactionServiceImpl extends AccountTransactionServiceImpl<GlTransaction, GlTransactionRepository>
        implements GlTransactionService {

    @Autowired
    private GlTransactionRepository glTransactionRepository;

    @Override
    public GlTransactionRepository getRepository() {
        return glTransactionRepository;
    }

    @Override
    public <D extends CreateUpdateDto<String>> void copyAdditionalPropertiesFromDtoToEntity(D source,
                                                                                            GlTransaction dest) {
        // Should do nothing because transaction details was build from original service to make it consistent.
    }
}
