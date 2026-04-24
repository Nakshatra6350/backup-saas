package com.nakshatra.backup_saas.common.util;

import java.util.UUID;

public class TraceIdUtil {

    private static final ThreadLocal<String> TRACE_ID = new ThreadLocal<>();

    public static String getTraceId() {
        return TRACE_ID.get();
    }

    public static void setTraceId(String traceId) {
        TRACE_ID.set(traceId);
    }

    public static void clear() {
        TRACE_ID.remove();
    }

    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
