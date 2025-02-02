package com.example.videojuegosapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.videojuegosapi.data.AuthManager
import com.example.videojuegosapi.ui.screens.ListaJuegosScreen
import com.example.videojuegosapi.ui.theme.screens.ForgotPasswordScreen
import com.example.videojuegosapi.ui.theme.screens.LoginScreen
import com.example.videojuegosapi.ui.theme.screens.SignUpScreen
import com.example.videojuegosapp.ui.screens.JuegoDetallesScreen

@Composable
fun Navigation(navController: NavHostController, auth: AuthManager) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                auth = auth,
                navigateToSignUp = { navController.navigate("signup") },
                navigateToHome = { navController.navigate("listaJuegos") },
                navigateToForgotPassword = { navController.navigate("forgot_password") }
            )
        }
        composable("signup") {
            SignUpScreen(
                auth = auth,
                navigateToLogin = { navController.navigate("login") },
                navigateToHome = { navController.navigate("listaJuegos") })
        }
        composable("forgot_password") {
            ForgotPasswordScreen(
                auth = auth,
                navigateToLogin = { navController.navigate("login") }
            )
        }
        composable("listaJuegos") {
            ListaJuegosScreen(
                navController = navController,
                auth = auth,  // Pasar instancia de AuthManager
                navigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("listaJuegos") { inclusive = true } // Elimina la pantalla anterior
                    }
                }
            )
        }
        composable("game_details/{idJuego}") { backStackEntry ->
            val idJuego = backStackEntry.arguments?.getString("idJuego")
            JuegoDetallesScreen(navController = navController, idJuego = idJuego)
        }
    }
}
