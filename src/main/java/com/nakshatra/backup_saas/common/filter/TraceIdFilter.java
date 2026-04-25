package com.nakshatra.backup_saas.common.filter;

import com.nakshatra.backup_saas.common.util.TraceIdUtil;
import jakarta.servlet.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            String traceId = TraceIdUtil.generateTraceId();
            TraceIdUtil.setTraceId(traceId);

            chain.doFilter(request, response);
        } finally {
            TraceIdUtil.clear();
        }
    }
}