package tech.corefinance.common.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralApiResponse<T> implements Serializable {

    /**
     * Generated!
     */
    private static final long serialVersionUID = -6412669348869863966L;

    public static int STATUS_SUCCESS = 0;
    public static int STATUS_UNKNOWN_ERROR = 1;

    private String statusCode;
    private int status;
    private T result;
    private Long processingMilliseconds;
    private Map<String, String[]> errorKeysWithData = null;

    public GeneralApiResponse(String statusCode, int status, T result) {
        this.statusCode = statusCode;
        this.status = status;
        this.result = result;
    }

    public GeneralApiResponse(T result) {
        this("", STATUS_SUCCESS, result);
    }

    public static GeneralApiResponse<String> createErrorResponseWithCode(String errorCode) {
        return new GeneralApiResponse<String>(errorCode, STATUS_UNKNOWN_ERROR, null);
    }

    public static GeneralApiResponse<String> createErrorResponseWithCode(Enum<?> errorCode) {
        return createErrorResponseWithCode(errorCode.name());
    }

    public static <T> GeneralApiResponse<T> createSuccessResponse(T result) {
        return new GeneralApiResponse<T>("OK", STATUS_SUCCESS, result);
    }

    public static <T> GeneralApiResponse<T> createErrorResponseWithCode(String errorCode, T result) {
        return new GeneralApiResponse<T>(errorCode, STATUS_UNKNOWN_ERROR, null);
    }
}
