package com.example.videojuegosapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videojuegosapi.data.network.ApiClient
import com.example.videojuegosapi.data.objetos.DetalleJuegos
import com.example.videojuegosapi.data.objetos.Juego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Clase de ViewModel para el manejo de datos de la aplicación
class JuegosViewModel : ViewModel() {
    private val _juegos = MutableStateFlow<List<Juego>>(emptyList())
    val juegos: StateFlow<List<Juego>> = _juegos

    // Cargar la lista de juegos
    fun cargarJuegos() {
        viewModelScope.launch {
            try {
                val juegosCargados = ApiClient.getGames()
                if (juegosCargados.isNotEmpty()) {
                    _juegos.value = juegosCargados
                } else {
                    println("Error: Lista de juegos vacía")
                }
            } catch (e: Exception) {
                println("Error al cargar los juegos: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}