package com.example.videojuegosapi.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.videojuegosapi.data.AuthManager
import com.example.videojuegosapi.data.FirestoreManager
import com.example.videojuegosapi.ui.screens.*
import com.example.videojuegosapi.ui.screens.Consolas.ListaConsolasScreen
import com.example.videojuegosapi.ui.screens.DetalleJuegos.DetalleJuegosViewModel
import com.example.videojuegosapi.ui.screens.DetalleJuegos.DetalleJuegosViewModelFactory
import com.example.videojuegosapi.ui.screens.ListaJuegos.JuegosViewModel
import com.example.videojuegosapi.ui.screens.ListaJuegos.JuegosViewModelFactory
import com.example.videojuegosapi.ui.screens.ListaJuegos.ListaJuegosScreen

@Composable
fun Navigation(
    navController: NavHostController,
    auth: AuthManager,
    firestoreManager: FirestoreManager
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                auth = auth,
                navigateToSignUp = { navController.navigate("signup") },
                navigateToHome = { navController.navigate("listaConsolas") },
                navigateToForgotPassword = { navController.navigate("forgot_password") }
            )
        }
        composable("signup") {
            SignUpScreen(
                auth = auth,
                navigateToLogin = { navController.navigate("login") },
                navigateToHome = { navController.navigate("listaConsolas") }
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(
                auth = auth,
                navigateToLogin = { navController.navigate("login") }
            )
        }
        composable("listaConsolas") {
            ListaConsolasScreen(
                navController = navController,
                auth = auth,
                firestoreManager = firestoreManager,
                navigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("listaConsolas") { inclusive = true }
                    }
                }
            )
        }

        //Navigations que pasan el ViewModelFactory
        composable("listaJuegos/{nombreConsola}") { backStackEntry ->
            val nombreConsola = backStackEntry.arguments?.getString("nombreConsola") ?: ""
            Log.d("Navigation", "Navegando a listaJuegos con nombre: ${nombreConsola}")

            val viewModel: JuegosViewModel = ViewModelProvider(
                backStackEntry,
                JuegosViewModelFactory(firestoreManager) // Pasar FirestoreManager si es necesario
            ).get(JuegosViewModel::class.java)

            ListaJuegosScreen(
                navController = navController,
                auth = auth,
                navigateToLogin = {
                    navController.navigate("login") {
                        popUpTo("listaJuegos") { inclusive = true }
                    }
                },
                nombreConsola = nombreConsola,
                viewModel = viewModel
            )
        }

        composable("game_details/{NombreJuego}") { backStackEntry ->
            val NombreJuego = backStackEntry.arguments?.getString("NombreJuego") ?: ""

            val viewModel: DetalleJuegosViewModel = ViewModelProvider(
                backStackEntry,
                DetalleJuegosViewModelFactory(firestoreManager) // Pasar FirestoreManager
            ).get(DetalleJuegosViewModel::class.java)

            Log.d("NavDetalles", "ID del juego: $NombreJuego")
            JuegoDetallesScreen(
                navController = navController,
                NombreJuego = NombreJuego,
                firestoreManager = firestoreManager, // Puede ser necesario pasarlo aquí también
                viewModel = viewModel
            )
        }
    }
}