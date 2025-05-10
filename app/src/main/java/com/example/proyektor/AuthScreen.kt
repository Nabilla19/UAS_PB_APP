package com.example.proyektor

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.proyektor.ui.theme.ProyektorTheme

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProyektorTheme {
                AuthScreen(onNavigateToHome = { name, email ->

                    MainActivity.saveUserData(this, name, email)

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                })
            }
        }
    }
}

@Composable
fun AuthScreen(
    onNavigateToHome: (String, String) -> Unit
) {
    var showLogin by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (showLogin) {
            LoginScreen(
                onLoginSuccess = { email ->
                    val name = email.substringBefore("@").replaceFirstChar { it.uppercase() }
                    onNavigateToHome(name, email)
                },
                onSwitchToRegister = { showLogin = false }
            )
        } else {
            RegisterScreen(
                onRegisterSuccess = { name, email ->
                    onNavigateToHome(name, email)
                },
                onSwitchToLogin = { showLogin = true }
            )
        }
    }
}
