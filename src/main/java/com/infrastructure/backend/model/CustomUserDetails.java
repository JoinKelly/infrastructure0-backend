package com.infrastructure.backend.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private String fullName;
    private String email;

    public CustomUserDetails(com.infrastructure.backend.entity.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.email = user.getEmail();
        this.fullName = user.getEmail();
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
