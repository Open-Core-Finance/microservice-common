package tech.corefinance.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.product.repository.DepositProductRepository;

@Service
@Transactional
public class DepositProductServiceImpl implements DepositProductService {

    @Autowired
    private DepositProductRepository depositProductRepository;

    @Override
    public DepositProductRepository getRepository() {
        return depositProductRepository;
    }
}
