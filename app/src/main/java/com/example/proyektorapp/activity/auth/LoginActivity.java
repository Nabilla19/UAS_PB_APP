package com.example.proyektorapp.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyektorapp.R;
import com.example.proyektorapp.activity.nav.DashboardActivity;
import com.example.proyektorapp.api.ApiClient;
import com.example.proyektorapp.api.service.auth.AuthService;
import com.example.proyektorapp.activity.auth.model.LoginRequest;
import com.example.proyektorapp.activity.auth.model.UserResponse;
import com.example.proyektorapp.helper.SharedPrefsHelper;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        authService = ApiClient.getClient().create(AuthService.class);

        btnLogin.setOnClickListener(view -> {
            String email = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            Log.d("LoginActivity", "Tombol login ditekan");
            Log.d("LoginActivity", "Email: " + email);

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Username dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Username atau password kosong");
                return;
            }

            LoginRequest request = new LoginRequest(email, password);
            Log.d("LoginActivity", "Mengirim permintaan login ke server...");

            authService.login(request).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserResponse userResponse = response.body();
                        Log.d("LoginDebug", userResponse.toString());
                        // Simpan semua data
                        SharedPrefsHelper prefs = new SharedPrefsHelper(LoginActivity.this);
                        prefs.saveToken(userResponse.getToken());
                        prefs.saveName(userResponse.getName());
                        prefs.saveEmail(userResponse.getEmail());
                        prefs.saveRole(userResponse.getRole());
                        prefs.saveId(String.valueOf(userResponse.getId()));

                        // Log semua data
                        Log.d("LoginDebug", "ID: " + userResponse.getId());
                        Log.d("LoginDebug", "Name: " + userResponse.getName());
                        Log.d("LoginDebug", "Email: " + userResponse.getEmail());
                        Log.d("LoginDebug", "Role: " + userResponse.getRole());
                        Log.d("LoginDebug", "Token: " + userResponse.getToken());

                        Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                        Log.d("LoginActivity", "Login berhasil, pindah ke DashboardActivity");
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Login gagal: " + response.message(), Toast.LENGTH_SHORT).show();
                        Log.e("LoginActivity", "Login gagal - Response tidak berhasil atau body kosong");
                        Log.e("LoginActivity", "Status code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("LoginActivity", "Login error: ", t);
                }
            });
        });


        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }
}
