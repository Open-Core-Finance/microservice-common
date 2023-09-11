package tech.corefinance.common.test.support.pojo;

import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.service.JwtVerifyAddOn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DummyJwtVerifyAddOn implements JwtVerifyAddOn {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public JwtTokenDto additionalJwtVerify(JwtTokenDto jwtTokenDto, String token, String deviceId, String ipaddress) {
        logger.debug("Running dummy JwtVerifyAddOn...");
        return jwtTokenDto;
    }
}