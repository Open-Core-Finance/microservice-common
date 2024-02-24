package tech.corefinance.account.gl.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.account.gl.entity.GlTransaction;
import tech.corefinance.common.repository.CommonResourceRepository;

@Repository
public interface GlTransactionRepository extends CommonResourceRepository<GlTransaction, String> {
}
