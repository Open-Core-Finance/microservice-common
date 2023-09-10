package com.finance.core.common.service;

import com.finance.core.common.dto.JwtTokenDto;

public interface JwtVerifyAddOn {

    JwtTokenDto additionalJwtVerify(JwtTokenDto jwtTokenDto, String token, String deviceId,
                                    String ipaddress);
}
