package com.nakshatra.backup_saas.common.exception;

import com.nakshatra.backup_saas.common.response.ApiResponse;
import com.nakshatra.backup_saas.common.response.ApiStatus;
import com.nakshatra.backup_saas.common.util.TraceIdUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiResponse<Object> buildResponse(String message, int statusCode) {
        return ApiResponse.builder()
                .status(ApiStatus.ERROR)
                .statusCode(statusCode)
                .message(message)
                .traceId(TraceIdUtil.getTraceId())
                .build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(BadRequestException ex) {
        return ResponseEntity.badRequest().body(buildResponse(ex.getMessage(), 400));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(401).body(buildResponse(ex.getMessage(), 401));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(404).body(buildResponse(ex.getMessage(), 404));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Object>> handleConflict(ConflictException ex) {
        return ResponseEntity.status(409).body(buildResponse(ex.getMessage(), 409));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception ex) {
        System.out.println(ex.getMessage());
        return ResponseEntity.internalServerError()
                .body(buildResponse("Internal Server Error, Something went wrong", 500));
    }
}