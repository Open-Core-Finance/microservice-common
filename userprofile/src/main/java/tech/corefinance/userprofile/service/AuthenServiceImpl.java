package tech.corefinance.userprofile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.corefinance.userprofile.common.repository.CustomLoginSessionRepository;
import tech.corefinance.userprofile.common.service.impl.AbstractAuthenService;
import tech.corefinance.userprofile.entity.LoginSession;
import tech.corefinance.userprofile.entity.UserProfile;

@Service
@Transactional
@ConditionalOnProperty(prefix = "tech.corefinance.security", name = "authorize-check", havingValue = "true",
        matchIfMissing = true)
public class AuthenServiceImpl extends AbstractAuthenService<UserProfile, LoginSession> {

    @Autowired
    private CustomLoginSessionRepository customLoginSessionRepository;

    @Override
    public LoginSession createEmptySession() {
        return new LoginSession();
    }

    @Override
    protected int invalidateOldLogins(String verifyKey) {
        return customLoginSessionRepository.invalidateOldLogins(verifyKey);
    }
}
