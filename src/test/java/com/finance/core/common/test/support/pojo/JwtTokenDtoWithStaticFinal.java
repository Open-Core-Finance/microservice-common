package com.finance.core.common.test.support.pojo;

import com.finance.core.common.dto.JwtTokenDto;
import com.finance.core.common.enums.AppPlatform;
import com.finance.core.common.model.AppVersion;

public class JwtTokenDtoWithStaticFinal extends JwtTokenDto {

    private static final String ABC = "ABC";

    public JwtTokenDtoWithStaticFinal() {
        super("", "", "", AppPlatform.IOS, new AppVersion(), "", "");
    }
}
