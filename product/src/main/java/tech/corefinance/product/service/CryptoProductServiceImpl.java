package tech.corefinance.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.product.repository.CryptoProductRepository;

@Service
@Transactional
public class CryptoProductServiceImpl implements CryptoProductService {

    @Autowired
    private CryptoProductRepository cryptoProductRepository;
    @Override
    public CryptoProductRepository getRepository() {
        return cryptoProductRepository;
    }
}
