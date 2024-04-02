package com.icis.demo.System;

import com.icis.demo.Entity.OnlineUser;

public class AuthenticationResponse {
    private boolean success;
    private String message;
    private OnlineUser onlineUser;

    public AuthenticationResponse(boolean success, String message, OnlineUser onlineUser) {
        this.success = success;
        this.message = message;
        this.onlineUser = onlineUser;
    }

    public AuthenticationResponse() {
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OnlineUser getOnlineUser() {
        return onlineUser;
    }

    public void setOnlineUser(OnlineUser onlineUser) {
        this.onlineUser = onlineUser;
    }
}
