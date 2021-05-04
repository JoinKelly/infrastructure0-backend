package com.infrastructure.backend.model.user.request;

import javax.validation.constraints.NotBlank;

public class UserLogin {

    private String username;

    @NotBlank
    private String password;

    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
