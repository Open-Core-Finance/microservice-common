package tech.corefinance.common.test.support.pojo;

import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.AppVersion;

public class JwtTokenDtoWithStaticFinal extends JwtTokenDto {

    private static final String ABC = "ABC";

    public JwtTokenDtoWithStaticFinal() {
        super("", "", "", AppPlatform.IOS, new AppVersion(), "", "");
    }
}
