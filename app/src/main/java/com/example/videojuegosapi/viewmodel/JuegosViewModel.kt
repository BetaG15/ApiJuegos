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

    private val _detalleJuego = MutableStateFlow<DetalleJuegos?>(null)
    val detalleJuego: StateFlow<DetalleJuegos?> = _detalleJuego

    // Cargar la lista de juegos
    fun cargarJuegos() {
        viewModelScope.launch {
            try {
                val juegosCargados = ApiClient.getGames()
                _juegos.value = juegosCargados
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Cargar los detalles de un juego
    fun cargarDetalleJuego(gameId: String) {
        viewModelScope.launch {
            try {
                val detalle = ApiClient.getGameDetails(gameId)
                _detalleJuego.value = detalle
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}