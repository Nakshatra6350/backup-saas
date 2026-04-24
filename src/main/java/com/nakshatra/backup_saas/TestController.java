package com.nakshatra.backup_saas;

import com.nakshatra.backup_saas.common.response.ApiResponse;
import com.nakshatra.backup_saas.common.response.ResponseUtil;
import com.nakshatra.backup_saas.common.util.TraceIdUtil;
import com.nakshatra.backup_saas.security.dto.LoginResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public ApiResponse<String> test() {
        return ResponseUtil.success("Working");
    }

    @GetMapping("/error")
    public String error() {
        throw new RuntimeException("Something broke");
    }
}