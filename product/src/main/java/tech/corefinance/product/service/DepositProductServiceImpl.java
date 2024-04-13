package tech.corefinance.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.product.common.dto.DepositProductDto;
import tech.corefinance.product.entity.DepositProduct;
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

    @Override
    public <D extends CreateUpdateDto<String>> DepositProduct copyAdditionalPropertiesFromDtoToEntity(D source,
                                                                                            DepositProduct dest) {
        dest = DepositProductService.super.copyAdditionalPropertiesFromDtoToEntity(source, dest);
        if (source instanceof DepositProductDto dto) {
            if (!dto.isAutoSetAsDormant()) {
                dest.setDaysToSetToDormant(null);
            }
            if (!dto.isEnableEarlyClosurePeriod()) {
                dest.setEarlyClosurePeriod(null);
            }
            if (!dto.isEnableInterestRate()) {
                dest.setInterestRate(null);
            }
        }
        return dest;
    }
}
