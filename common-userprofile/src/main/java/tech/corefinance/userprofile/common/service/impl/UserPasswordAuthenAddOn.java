package tech.corefinance.userprofile.common.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.corefinance.common.service.JwtService;
import tech.corefinance.userprofile.common.entity.CommonUserProfile;
import tech.corefinance.userprofile.common.entity_author.CommonLoginSession;
import tech.corefinance.userprofile.common.service.UserAuthenAddOn;

import java.util.Map;

@Service
@ConditionalOnProperty(prefix = "tech.corefinance.app.userprofile", name = "default-password-authen", havingValue = "true",
        matchIfMissing = true)
@Slf4j
@RequiredArgsConstructor
public class UserPasswordAuthenAddOn implements UserAuthenAddOn<CommonUserProfile<?>, CommonLoginSession<?>> {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public CommonUserProfile<?> authenticate(CommonUserProfile<?> userprofile, String account, String password,
                                             Map<String, Object> additionalInfo) {
        log.debug("Comparing input password [{}] and user password [{}]", password, userprofile.getPassword());
        boolean result = passwordEncoder.matches(password, userprofile.getPassword());
        log.debug("Comparing result [{}]", result);
        if (result) {
            log.debug("Return user [{}]", userprofile);
            return userprofile;
        }
        log.debug("Return null for user [{}]!!", account);
        return null;
    }

}
