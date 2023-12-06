import { LanguageItem } from "./LanguageItem";

export class AppSettings {
    public static LOCAL_KEY_SAVED_LANGUAGE ='selectedLanguage';
    public static LOCAL_KEY_SAVED_CREDENTIAL ='credential';

    public static LANGUAGE_DEFAULT = new LanguageItem("vn", "Việt Nam");

    public static REST_HEADER_DEVICE_ID = "x-device-id";
    public static REST_HEADER_TRACE_ID = "x-trace-id";
    public static REST_HEADER_CLIENT_APP_ID = "x-client-id";
    public static REST_HEADER_APP_PLATFORM = "x-app-platform";
    public static REST_HEADER_APP_VERSION = "x-app-version";
    public static REST_HEADER_LANGUAGE_KEY = "x-lang-key";
    public static REST_HEADER_SKIP_CORS = "x-skip-cors";
 }