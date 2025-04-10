package tech.corefinance.userprofile.repository;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import tech.corefinance.userprofile.common.repository.CommonLoginSessionRepository;
import tech.corefinance.userprofile.entity.LoginSession;

@Repository
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public interface LoginSessionRepository extends CommonLoginSessionRepository<LoginSession> {
}
