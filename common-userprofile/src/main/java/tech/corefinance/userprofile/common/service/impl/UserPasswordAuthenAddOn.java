package tech.corefinance.userprofile.common.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.corefinance.common.service.JwtService;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;
import tech.corefinance.userprofile.common.repository.CommonUserProfileRepository;
import tech.corefinance.userprofile.common.service.UserAuthenAddOn;

import java.util.Map;
import java.util.Optional;

@Service
@ConditionalOnProperty(prefix = "tech.corefinance.app.userprofile", name = "default-password-authen", havingValue = "true",
        matchIfMissing = true)
@Slf4j
public class UserPasswordAuthenAddOn implements UserAuthenAddOn<CommonUserProfile<?>> {

    @SuppressWarnings("rawtypes")
    @Autowired
    private CommonUserProfileRepository commonUserProfileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public CommonUserProfile authenticate(String account, String password, Map<String, Object> additionalInfo) {
        Optional<CommonUserProfile> optional = commonUserProfileRepository.findFirstByEmailIgnoreCaseOrUsernameIgnoreCase(account, account);
        if (optional.isPresent()) {
            CommonUserProfile userprofile = optional.get();
            log.debug("Comparing input password [{}] and user password [{}]", password, userprofile.getPassword());
            boolean result = passwordEncoder.matches(password, userprofile.getPassword());
            log.debug("Comparing result [{}]", result);
            if (result) {
                log.debug("Return user [{}]", userprofile);
                return userprofile;
            }
        }
        log.debug("Return null for user [{}]!!", account);
        return null;
    }

    @Override
    public JwtService getJwtService() {
        return jwtService;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
