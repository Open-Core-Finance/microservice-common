package com.finance.core.common.test.support.pojo;

import com.finance.core.common.dto.JwtTokenDto;
import com.finance.core.common.enums.AppPlatform;
import com.finance.core.common.model.AppVersion;
import lombok.Getter;

@Getter
public class JwtTokenDtoWithNonSerializable extends JwtTokenDto {

    private Object a = new Object();

    public JwtTokenDtoWithNonSerializable() {
        super("", "", "", AppPlatform.IOS, new AppVersion(), "", "");
    }
}
