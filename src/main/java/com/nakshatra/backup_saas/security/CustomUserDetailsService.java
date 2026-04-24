package com.nakshatra.backup_saas.security;

import com.nakshatra.backup_saas.common.context.TenantContext;
import com.nakshatra.backup_saas.tenant.User;
import com.nakshatra.backup_saas.tenant.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    private final JdbcTemplate jdbcTemplate;

    public CustomUserDetailsService(@Qualifier("masterJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new UsernameNotFoundException("Tenant not resolved");
        }

        try{
            User user = jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE email = ? AND tenant_id = ?",
                    new Object[]{email, tenantId},
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

            return org.springframework.security.core.userdetails.User
                    .withUsername(user.getEmail())
                    .password(user.getPasswordHash())
                    .roles(user.getRole())
                    .build();
        }catch (Exception e){
            throw new UsernameNotFoundException("User not found");
        }
    }



}
