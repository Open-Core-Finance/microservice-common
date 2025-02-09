package tech.corefinance.userprofile.repository;

import org.springframework.stereotype.Repository;
import tech.corefinance.userprofile.common.repository.CommonLoginSessionRepository;
import tech.corefinance.userprofile.entity.LoginSession;

@Repository
public interface LoginSessionRepository extends CommonLoginSessionRepository<LoginSession> {
}
