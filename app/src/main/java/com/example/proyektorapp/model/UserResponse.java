package com.example.proyektorapp.model;

public class UserResponse {
    private int id;
    private String name;
    private String email;
    private String role;
    private String token;

    // Getter methods
    public int getId(){
        return id;
    }
    public String getToken() {
        return token;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}