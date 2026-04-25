package com.nakshatra.backup_saas.security;

import com.nakshatra.backup_saas.common.context.TenantContext;
import com.nakshatra.backup_saas.common.exception.NotFoundException;
import com.nakshatra.backup_saas.common.response.ApiResponse;
import com.nakshatra.backup_saas.common.response.ResponseUtil;
import com.nakshatra.backup_saas.security.dto.LoginRequest;
import com.nakshatra.backup_saas.security.dto.LoginResponse;
import com.nakshatra.backup_saas.security.util.JwtUtil;
import com.nakshatra.backup_saas.tenant.entity.Tenant;
import com.nakshatra.backup_saas.tenant.repository.TenantRepository;
import com.nakshatra.backup_saas.tenant.entity.User;
import com.nakshatra.backup_saas.tenant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final TenantRepository tenantRepository;
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    public AuthController(@Qualifier("masterJdbcTemplate") JdbcTemplate jdbcTemplate,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder,
                          TenantRepository tenantRepository, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @RequestHeader("X-Tenant-Id") String tenantIdentifier,
            @RequestBody LoginRequest request) {

        Tenant tenant = tenantRepository.findByIdentifier(tenantIdentifier)
                .orElseThrow(() -> new NotFoundException("Tenant not found"));

        TenantContext.setTenantId(tenant.getId());

        try {
            // 3️⃣ Now JPA will use TENANT DB automatically
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 4️⃣ Password check
            if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
                throw new RuntimeException("Invalid credentials");
            }

            // 5️⃣ Generate JWT (include tenantId)
            String token = jwtUtil.generateToken(user.getEmail(), tenant.getId());

            return ResponseUtil.success(
                    LoginResponse.builder().token(token).build()
            );

        } finally {
            TenantContext.clear();
        }
    }
}