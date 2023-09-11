package tech.corefinance.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppPlatform {

    UNKNOWN(0), IOS(1), ANDROID(2), WEB(3);
    private int value;
}
