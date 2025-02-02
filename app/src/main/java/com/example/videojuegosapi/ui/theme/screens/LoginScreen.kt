package com.example.videojuegosapi.ui.theme.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.videojuegosapi.data.AuthManager
import com.example.videojuegosapi.data.AuthRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
    auth: AuthManager,
    navigateToSignUp: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Iniciar Sesión", fontSize = 24.sp)

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(10.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    scope.launch {
                        signIn(email, password, context, auth, navigateToHome)
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f).height(50.dp)
            ) {
                Text("Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "¿Olvidaste tu contraseña?",
                modifier = Modifier.clickable { navigateToForgotPassword() },
                style = TextStyle(
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Blue
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "¿No tienes cuenta? Regístrate",
                modifier = Modifier.clickable { navigateToSignUp() },
                style = TextStyle(
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Blue
                )
            )
        }
    }
}

suspend fun signIn(email: String, password: String, context: Context, auth: AuthManager, navigateToHome: () -> Unit) {
    if (email.isNotEmpty() && password.isNotEmpty()) {
        val result = withContext(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password)
        }
        when (result) {
            is AuthRes.Success -> {
                Toast.makeText(context, "Inicio de sesión correcto", Toast.LENGTH_SHORT).show()
                navigateToHome()
            }
            is AuthRes.Error -> {
                Toast.makeText(context, result.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    } else {
        Toast.makeText(context, "Email y contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show()
    }
}
