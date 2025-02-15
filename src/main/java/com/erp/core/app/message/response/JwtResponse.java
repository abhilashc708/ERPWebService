package com.erp.core.app.message.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String username;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean result;

    public JwtResponse(String accessToken, String username, Collection<? extends GrantedAuthority> authorities, boolean result) {
        this.token = accessToken;
        this.username = username;
        this.authorities = authorities;
        this.result = result;
    }
}
