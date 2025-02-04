package com.example.videojuegosapi.ui.screens.DetalleJuegos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videojuegosapi.data.network.ApiClient
import com.example.videojuegosapi.data.objetos.DetalleJuegos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Clase de ViewModel para el manejo de datos de la aplicación
class DetalleJuegosViewModel : ViewModel() {

    private val _detalleJuego = MutableStateFlow<DetalleJuegos?>(null)
    val detalleJuego: StateFlow<DetalleJuegos?> = _detalleJuego

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