package tech.corefinance.account.gl.service;

import tech.corefinance.account.common.service.AccountService;
import tech.corefinance.account.gl.entity.GlAccount;
import tech.corefinance.account.gl.repository.GlAccountRepository;
import tech.corefinance.common.service.CommonService;

public interface GlAccountService extends AccountService<GlAccount, GlAccountRepository>, CommonService<String, GlAccount, GlAccountRepository> {
    String SEQUENCE_NAME = "gl_account_id_seq";
}
