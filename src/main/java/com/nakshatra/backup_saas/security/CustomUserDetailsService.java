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


    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new UsernameNotFoundException("Tenant not resolved");
        }

        try{

            // 🔥 This will hit correct tenant DB via routing
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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
