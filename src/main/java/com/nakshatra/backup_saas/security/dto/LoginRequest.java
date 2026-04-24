package com.nakshatra.backup_saas.security.dto;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;
    private String password; // plain password from user
}