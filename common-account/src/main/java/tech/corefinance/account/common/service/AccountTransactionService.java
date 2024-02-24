package tech.corefinance.account.common.service;

import tech.corefinance.account.common.entity.AccountTransaction;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.common.service.CommonService;

public interface AccountTransactionService<T extends AccountTransaction, R extends CommonResourceRepository<T, String>>
        extends CommonService<String, T, R> {
}
