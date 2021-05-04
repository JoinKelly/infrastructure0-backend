package com.infrastructure.backend.model.user.request;

import javax.validation.constraints.NotBlank;

public class ChangePassword {

    @NotBlank(message = "Current password is empty")
    private String currentPassword;

    @NotBlank(message = "New password is empty")
    private String newPassword;

    @NotBlank(message = "Confirm password is empty")
    private String confirmPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
