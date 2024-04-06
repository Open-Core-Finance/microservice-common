package tech.corefinance.product.service;

import tech.corefinance.common.ex.ServiceProcessingException;
import tech.corefinance.common.model.CreateUpdateDto;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.common.service.CommonService;
import tech.corefinance.product.entity.Product;

public interface ProductService<T extends Product, R extends CommonResourceRepository<T, String>>
        extends CommonService<String, T, R> {

    @Override
    default <D extends CreateUpdateDto<String>> T customEntityValidation(D source, T dest) {
        dest = CommonService.super.customEntityValidation(source, dest);
        var currencies = dest.getCurrencies();
        if (currencies == null || currencies.length < 1) {
            throw new ServiceProcessingException("currencies_empty");
        }
        var newAccountSetting = dest.getNewAccountSetting();
        if (newAccountSetting == null || newAccountSetting.getType() == null) {
            throw new ServiceProcessingException("new_account_type_empty");
        }
        return dest;
    }
}
