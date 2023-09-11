package tech.corefinance.common.converter;

import tech.corefinance.common.enums.CommonConstants;
import tech.corefinance.common.model.AppVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StringToAppVersionConverterTest {

    private StringToAppVersionConverter converter;

    @BeforeEach
    void setUp() {
        converter = new StringToAppVersionConverter();
    }

    @Test
    void test_convert_happyCase() {
        AppVersion appVersion = converter.convert("1.2.3.DevBuild");
        Assertions.assertEquals(1, appVersion.getMajor());
        Assertions.assertEquals(2, appVersion.getMinor());
        Assertions.assertEquals(3, appVersion.getMaintenance());
        Assertions.assertEquals("DevBuild", appVersion.getBuild());
    }

    @Test
    void test_convert_invalidVersion() {
        AppVersion appVersion = converter.convert("this is invalid");
        Assertions.assertNull(appVersion);
    }

    @Test
    void test_convert_inputAsJson() {
        AppVersion appVersion = converter.convert(CommonConstants.DEFAULT_VERSION_JSON);
        Assertions.assertEquals(1, appVersion.getMajor());
        Assertions.assertEquals(0, appVersion.getMinor());
        Assertions.assertEquals(0, appVersion.getMaintenance());
        Assertions.assertEquals("0", appVersion.getBuild());
    }

    @Test
    void test_convert_nullVersion() {
        AppVersion appVersion = converter.convert(null);
        Assertions.assertNull(appVersion);
    }
}
