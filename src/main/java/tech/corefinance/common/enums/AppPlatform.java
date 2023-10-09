package tech.corefinance.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Client Application platform.
 */
@Getter
@AllArgsConstructor
public enum AppPlatform {

    /**
     * Unknown devices.
     */
    UNKNOWN(0),
    /**
     * IOS devices.
     */
    IOS(1),
    /**
     * Android devices.
     */
    ANDROID(2),
    /**
     * Web clients.
     */
    WEB(3);

    private int value;
}
