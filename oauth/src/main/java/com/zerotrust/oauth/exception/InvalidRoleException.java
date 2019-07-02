package com.zerotrust.oauth.exception;


import lombok.Getter;

public class InvalidRoleException extends Exception {
    @Getter
    private String role;

    public InvalidRoleException(String role) {
        this.role = role;
    }
}
