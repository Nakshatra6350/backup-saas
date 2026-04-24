package com.nakshatra.backup_saas.common.response;

import com.nakshatra.backup_saas.common.util.TraceIdUtil;

public final class ResponseUtil {

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(ApiStatus.SUCCESS)
                .statusCode(200)
                .data(data)
                .traceId(TraceIdUtil.getTraceId())
                .build();
    }

    public static <T> ApiResponse<T> created(T data) {
        return ApiResponse.<T>builder()
                .status(ApiStatus.SUCCESS)
                .statusCode(201)
                .data(data)
                .traceId(TraceIdUtil.getTraceId())
                .build();
    }

    public static ApiResponse<Void> successMessage(String message) {
        return ApiResponse.<Void>builder()
                .status(ApiStatus.SUCCESS)
                .statusCode(200)
                .message(message)
                .traceId(TraceIdUtil.getTraceId())
                .build();
    }
}