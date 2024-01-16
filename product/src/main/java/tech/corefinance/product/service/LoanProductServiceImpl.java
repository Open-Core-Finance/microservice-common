package tech.corefinance.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.product.repository.LoanProductRepository;

@Transactional
@Service
public class LoanProductServiceImpl implements LoanProductService {

    @Autowired
    private LoanProductRepository loanProductRepository;

    @Override
    public LoanProductRepository getRepository() {
        return loanProductRepository;
    }
}
