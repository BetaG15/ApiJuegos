package com.example.videojuegosapi.ui.screens.DetalleJuegos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.videojuegosapi.data.FirestoreManager
import com.example.videojuegosapi.data.model.Juego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetalleJuegosViewModel(private val firestoreManager: FirestoreManager) : ViewModel(),
    ViewModelProvider.Factory {

    private val _detalleJuego = MutableStateFlow<Juego?>(null)
    val detalleJuego: StateFlow<Juego?> = _detalleJuego

    // Cargar los detalles del juego por nombre
    fun cargarDetalleJuego(nombreJuego: String) {
        if (nombreJuego.isEmpty()) {
            Log.e("DetalleViewModel", "El nombre del juego está vacío")
            return
        }
        viewModelScope.launch {
            try {
                val juego = firestoreManager.getJuegoPorNombre(nombreJuego)
                _detalleJuego.value = juego
                Log.d("DetalleViewModel", "Juego cargado: ${juego?.nombre}")
            } catch (e: Exception) {
                Log.e("DetalleViewModel", "Error al cargar detalles del juego: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}

