package com.paymybuddy.project.security.model.response;

import javax.validation.constraints.Email;

public class JwtResponse {

    private Long id;

    @Email
    private String email;

    private String token;

    private String type = "Bearer";

    public JwtResponse(String accessToken, String email, Long id) {
        this.token = accessToken;
        this.email = email;
        this.id = id;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
