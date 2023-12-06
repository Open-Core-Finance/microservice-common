package tech.corefinance.common.test.support.pojo;

import tech.corefinance.common.dto.JwtTokenDto;
import tech.corefinance.common.enums.AppPlatform;
import tech.corefinance.common.model.AppVersion;
import lombok.Getter;

@Getter
public class JwtTokenDtoWithNonSerializable extends JwtTokenDto {

    private Object a = new Object();

    public JwtTokenDtoWithNonSerializable() {
        super("", "", "", AppPlatform.IOS, new AppVersion(), "", "");
    }
}
