package tech.corefinance.account.common.service;

import tech.corefinance.account.common.entity.Account;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.common.service.CommonService;

public interface AccountService<T extends Account, R extends CommonResourceRepository<T, String>>
        extends CommonService<String, T, R> {
}
