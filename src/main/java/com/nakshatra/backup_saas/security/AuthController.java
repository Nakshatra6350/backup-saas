package com.nakshatra.backup_saas.security;

import com.nakshatra.backup_saas.common.exception.NotFoundException;
import com.nakshatra.backup_saas.common.response.ApiResponse;
import com.nakshatra.backup_saas.common.response.ResponseUtil;
import com.nakshatra.backup_saas.common.util.TraceIdUtil;
import com.nakshatra.backup_saas.security.dto.LoginRequest;
import com.nakshatra.backup_saas.security.dto.LoginResponse;
import com.nakshatra.backup_saas.tenant.Tenant;
import com.nakshatra.backup_saas.tenant.TenantRepository;
import com.nakshatra.backup_saas.tenant.User;
import com.nakshatra.backup_saas.tenant.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final TenantRepository tenantRepository;
    private final JdbcTemplate jdbcTemplate;

    public AuthController(@Qualifier("masterJdbcTemplate") JdbcTemplate jdbcTemplate,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder,
                          TenantRepository tenantRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.tenantRepository = tenantRepository;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @RequestHeader("X-Tenant-Id") String tenantIdentifier,
            @RequestBody LoginRequest request) {

        Map<String, Object> tenant = jdbcTemplate.queryForMap(
                "SELECT * FROM tenants WHERE identifier = ?",
                tenantIdentifier
        );

        User user = jdbcTemplate.queryForObject(
                "SELECT * FROM users WHERE email = ? AND tenant_id = ?",
                new Object[]{request.getEmail(), tenant.get("id")},
                (rs, rowNum) -> {
                    User u = new User();
                    u.setId(rs.getLong("id"));
                    u.setEmail(rs.getString("email"));
                    u.setPasswordHash(rs.getString("password_hash"));
                    u.setRole(rs.getString("role"));
                    u.setTenantId(rs.getLong("tenant_id"));
                    return u;
                }
        );

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getTenantId());

        return ResponseUtil.success(
                LoginResponse.builder().token(token).build());
    }
}