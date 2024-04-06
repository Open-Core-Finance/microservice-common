package tech.corefinance.account.gl.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.account.gl.entity.GlAccount;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface GlAccountRepository extends CommonResourceRepository<GlAccount, String> {

}
