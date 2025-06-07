package com.example.proyektorapp.api;

import com.example.proyektorapp.model.LoginRequest;
import com.example.proyektorapp.model.RegisterRequest;
import com.example.proyektorapp.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("auth/login")
    Call<UserResponse> login(@Body LoginRequest request);

    @POST("auth/register")
    Call<UserResponse> register(@Body RegisterRequest request);
}
