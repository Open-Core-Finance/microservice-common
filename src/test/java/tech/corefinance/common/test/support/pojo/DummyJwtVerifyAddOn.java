package tech.corefinance.common.test.support.pojo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.service.JwtVerifyAddOn;

@Component
@Slf4j
public class DummyJwtVerifyAddOn implements JwtVerifyAddOn {

    @Override
    public JwtTokenDto additionalJwtVerify(JwtTokenDto jwtTokenDto, String token, String deviceId, String ipaddress) {
        log.debug("Running dummy JwtVerifyAddOn...");
        return jwtTokenDto;
    }
}
