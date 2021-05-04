package com.infrastructure.backend.model.user.response;

public class UserTokenState {
    private String access_token;
    private Long expiresIn;

    public UserTokenState() {
    }

    public UserTokenState(String access_token, long expiresIn) {
        this.access_token = access_token;
        this.expiresIn = expiresIn;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}