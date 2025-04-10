package tech.corefinance.userprofile.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import tech.corefinance.userprofile.common.repository.CustomLoginSessionRepository;

@Repository
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public class CustomLoginSessionRepositoryImpl implements CustomLoginSessionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int invalidateOldLogins(String verifyKey) {
        return entityManager.createQuery("update LoginSession ls set ls.validToken = false where ls.verifyKey = :verifyKey")
                .setParameter("verifyKey", verifyKey).executeUpdate();
    }
}