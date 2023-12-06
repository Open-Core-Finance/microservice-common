package tech.corefinance.userprofile.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.userprofile.entity.AttemptedLogin;

@Repository
public interface AttemptedLoginRepository extends CommonResourceRepository<AttemptedLogin, String> {
    long countByAccountAndEnabled(String account, boolean enabled);

    @Modifying
    @Query("update AttemptedLogin u set u.enabled=:enabled where lower(account) like lower(:username) or " +
            "lower(account) like lower(:email)")
    void updateEnabledByAccount(@Param("enabled") boolean enabled, @Param("username") String username,
                                @Param("email") String email);
}
