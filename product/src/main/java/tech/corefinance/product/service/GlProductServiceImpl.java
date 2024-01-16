package tech.corefinance.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.product.repository.GlProductRepository;

@Service
@Transactional
public class GlProductServiceImpl implements GlProductService {

    @Autowired
    private GlProductRepository glProductRepository;

    @Override
    public GlProductRepository getRepository() {
        return glProductRepository;
    }
}
