package tech.corefinance.userprofile.common.repository;

import org.springframework.data.repository.query.Param;

public interface CustomLoginSessionRepository {

    int invalidateOldLogins(@Param("verifyKey") String verifyKey);
}
