package tech.corefinance.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.payment.repository.InternalFundTransferRepository;

@Transactional
@Service
public class InternalFundTransferServiceImpl implements InternalFundTransferService {

    @Autowired
    private InternalFundTransferRepository internalFundTransferRepository;

    @Override
    public InternalFundTransferRepository getRepository() {
        return internalFundTransferRepository;
    }
}
