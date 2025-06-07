package com.example.proyektorapp.activity.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyektorapp.R;
import com.example.proyektorapp.api.ApiClient;
import com.example.proyektorapp.api.AuthService;
import com.example.proyektorapp.model.RegisterRequest;
import com.example.proyektorapp.model.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnSignIn;
    private TextView tvLog;
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        tvLog = findViewById(R.id.tvLog);

        authService = ApiClient.getClient().create(AuthService.class);

        btnSignIn.setOnClickListener(v -> {
            String name = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignInActivity.this, "Semua kolom wajib diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(SignInActivity.this, "Format email tidak valid!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignInActivity.this, "Password tidak sama!", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterRequest request = new RegisterRequest(name, email, password);
            authService.register(request).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(SignInActivity.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Gagal registrasi: " + response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(SignInActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        tvLog.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
