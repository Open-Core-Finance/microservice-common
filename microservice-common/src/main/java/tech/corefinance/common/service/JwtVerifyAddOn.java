package tech.corefinance.common.service;

import tech.corefinance.common.dto.JwtTokenDto;

public interface JwtVerifyAddOn {

    JwtTokenDto additionalJwtVerify(JwtTokenDto jwtTokenDto, String token, String deviceId, String ipaddress);
}
