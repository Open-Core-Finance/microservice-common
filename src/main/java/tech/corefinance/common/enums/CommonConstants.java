package tech.corefinance.common.enums;

public final class CommonConstants {

    private CommonConstants() {
        // Private constructor to prevent object construction.
        throw new IllegalStateException("Utility class");
    }

    public static final String LANGUAGE_HEADER_KEY = "x-lang_key";

    public static final String DEFAULT_LANGUAGE_HEADER = "en";

    public static final String HEADER_KEY_TRACE_ID = "x-trace-id";
    public static final String HEADER_KEY_TENANT_ID = "x-tenant-id";
    public static final String HEADER_KEY_CLIENT_APP_ID = "x-client-id";
    public static final String HEADER_KEY_APP_PLATFORM = "x-app-platform";
    public static final String HEADER_KEY_APP_VERSION = "x-app-version";
    public static final String HEADER_KEY_INTERNAL_API_KEY = "x-internal-api-key";

    public static final String DEVICE_ID = "x-device-id";
    public static final String EXTERNAL_IP_ADDRESS = "x-external-ip";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String ATTRIBUTE_NAME_DEVICE_ID = "deviceId";
    public static final String ATTRIBUTE_NAME_IP_ADDRESS = "loginIpAddr";
    public static final String ATTRIBUTE_NAME_APP_VERSION = "appVersion";
    public static final String ATTRIBUTE_NAME_APP_PLATFORM = "appPlatform";

    public static final String JWT_VERIFY_MODE_SINGLE_LOGIN = "SINGLE_LOGIN";
    public static final String JWT_VERIFY_MODE_SINGLE_LOGIN_PER_APP = "SINGLE_LOGIN_PER_APP";
    public static final String JWT_VERIFY_MODE_SINGLE_LOGIN_PER_DEVICE = "SINGLE_LOGIN_PER_DEVICE";
    public static final String JWT_VERIFY_MODE_MULTIPLE_LOGIN = "MULTIPLE_LOGIN";

    public static final String DEFAULT_VERSION_JSON = "{\"major\":1,\"minor\":0,\"maintenance\":0, \"build\":\"-ALPHA\"}";
    /**
     * Define using string builder to prevent sonar recognize as IP address which is not.
     */
    public static final String DEFAULT_VERSION_NUMBER_SEPARATOR = ".";
    public static final String DEFAULT_VERSION_BUILD_SEPARATOR = "-";
    public static final String DEFAULT_VERSION_STRING = new StringBuilder("1").append(DEFAULT_VERSION_NUMBER_SEPARATOR).append("0")
            .append(DEFAULT_VERSION_NUMBER_SEPARATOR).append("0").append(DEFAULT_VERSION_BUILD_SEPARATOR).append("ALPHA").toString();
    public static final String DEFAULT_APP_PLATFORM_STRING = "WEB";
    public static final String DEFAULT_CLIENT_APP_ID = "1";
    public static final String LOGBACK_FILE_PATH_KEY = "logFilePath";
    public static final String LOGBACK_FILE_NAME_KEY = "logFileName";
    public static final String LOGBACK_FILE_PATH_DEFAULT = "logs";
    public static final String LOGBACK_FILE_NAME_DEFAULT = "corefinance";
}
