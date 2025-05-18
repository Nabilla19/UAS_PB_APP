package com.example.proyektorapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowInsetsController;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister; // Tambahan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Fullscreen status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(false);
            View decorView = getWindow().getDecorView();
            WindowInsetsController controller = decorView.getWindowInsetsController();
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        // Inisialisasi view
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister); // Tambahan

        // Aksi tombol login
        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Username dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            } else {
                if (username.equals("nia") && password.equals("123")) {
                    Toast.makeText(LoginActivity.this, "Login berhasil!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();  // biar tidak bisa kembali ke login dengan back button
                } else {
                    Toast.makeText(LoginActivity.this, "Username atau Password salah!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Aksi tombol ke Sign Up
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }
}
