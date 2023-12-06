package tech.corefinance.userprofile.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.userprofile.entity.LoginSession;

import java.util.Optional;

@Repository
public interface LoginSessionRepository extends CommonResourceRepository<LoginSession, String> {

    Optional<LoginSession> findByIdAndRefreshToken(String loginId, String refreshToken);

    @Modifying
    @Query("update LoginSession ls set ls.validToken = false where ls.verifyKey = :verifyKey")
    void invalidateOldLogins(@Param("verifyKey") String verifyKey);
}
