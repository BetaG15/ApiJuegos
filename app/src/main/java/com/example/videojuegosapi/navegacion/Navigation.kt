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
    NavHost(navController = navController, startDestination = Login.toString()) {
        composable(Login.toString()) {
            LoginScreen(
                auth = auth,
                navigateToSignUp = { navController.navigate(SignUp.toString()) },
                navigateToHome = { navController.navigate(ListaConsolas.toString()) },
                navigateToForgotPassword = { navController.navigate(ForgotPassword.toString()) }
            )
        }
        composable(SignUp.toString()) {
            SignUpScreen(
                auth = auth,
                navigateToLogin = { navController.navigate(Login.toString()) },
                navigateToHome = { navController.navigate(ListaConsolas.toString()) }
            )
        }
        composable(ForgotPassword.toString()) {
            ForgotPasswordScreen(
                auth = auth,
                navigateToLogin = { navController.navigate(Login.toString()) }
            )
        }
        composable(ListaConsolas.toString()) {
            ListaConsolasScreen(
                navController = navController,
                auth = auth,
                firestoreManager = firestoreManager,
                navigateToLogin = {
                    navController.navigate(Login.toString()) {
                        popUpTo(ListaConsolas.toString()) { inclusive = true }
                    }
                }
            )
        }

        composable("listaJuegos/{nombreConsola}") { backStackEntry ->
            val nombreConsola = backStackEntry.arguments?.getString("nombreConsola") ?: ""
            Log.d("Navigation", "Navegando a listaJuegos con nombre: $nombreConsola")

            val viewModel: JuegosViewModel = ViewModelProvider(
                backStackEntry,
                JuegosViewModelFactory(firestoreManager)
            ).get(JuegosViewModel::class.java)

            ListaJuegosScreen(
                navController = navController,
                auth = auth,
                navigateToLogin = {
                    navController.navigate(Login.toString()) {
                        popUpTo("listaJuegos") { inclusive = true }
                    }
                },
                nombreConsola = nombreConsola,
                viewModel = viewModel
            )
        }

        composable("game_details/{NombreJuego}") { backStackEntry ->
            val nombreJuego = backStackEntry.arguments?.getString("NombreJuego") ?: ""

            val viewModel: DetalleJuegosViewModel = ViewModelProvider(
                backStackEntry,
                DetalleJuegosViewModelFactory(firestoreManager)
            ).get(DetalleJuegosViewModel::class.java)

            Log.d("NavDetalles", "ID del juego: $nombreJuego")
            JuegoDetallesScreen(
                navController = navController,
                NombreJuego = nombreJuego,
                firestoreManager = firestoreManager,
                viewModel = viewModel
            )
        }
    }
}
