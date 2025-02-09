package tech.corefinance.userprofile.common.repository;

import org.springframework.data.repository.NoRepositoryBean;
import tech.corefinance.common.repository.CommonResourceRepository;
import tech.corefinance.userprofile.common.entity.CommonLoginSession;

import java.util.Optional;

@NoRepositoryBean
public interface CommonLoginSessionRepository<T extends CommonLoginSession<?>> extends CommonResourceRepository<T, String> {

    Optional<T> findByIdAndRefreshToken(String loginId, String refreshToken);
}
