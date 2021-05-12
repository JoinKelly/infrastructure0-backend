package com.infrastructure.backend.model.common.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserUpdateRequest {

    @NotBlank(message = "FullName is mandatory")
    private String fullName;

    @NotBlank(message = "Email is mandatory")
    @Email
    private String email;

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
