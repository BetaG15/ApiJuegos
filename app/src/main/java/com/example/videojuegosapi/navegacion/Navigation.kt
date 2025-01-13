package com.example.videojuegosapi.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.videojuegosapi.ui.screens.ListaJuegosScreen
import com.example.videojuegosapp.ui.screens.JuegoDetallesScreen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "listaJuegos") {
        composable("listaJuegos") {
            ListaJuegosScreen(navController = navController)
        }
        composable("game_details/{idJuego}") { backStackEntry ->
            val idJuego = backStackEntry.arguments?.getString("idJuego")
            JuegoDetallesScreen(navController = navController, idJuego = idJuego)
        }
    }
}