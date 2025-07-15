package com.example.ecommercebackend.entities;

public class JwtAuthResponse {
    private String token;
    private User user;

    public JwtAuthResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public User getUser() {
        return user;
    }
}

