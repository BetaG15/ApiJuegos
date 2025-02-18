package com.example.videojuegosapi.ui.screens.ListaJuegos

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videojuegosapi.data.FirestoreManager
import com.example.videojuegosapi.data.model.Juego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JuegosViewModel(private val firestoreManager: FirestoreManager) : ViewModel() {

    private val _juegos = MutableStateFlow<List<Juego>>(emptyList())
    val juegos: StateFlow<List<Juego>> = _juegos

    // Función para cargar juegos filtrados por consola
    fun cargarJuegos(nombreConsola: String) {
        viewModelScope.launch {
            try {
                firestoreManager.getJuegos(nombreConsola).collect { juegosList ->
                    _juegos.value = juegosList
                }
            } catch (e: Exception) {
                println("Error al cargar los juegos: ${e.message}")
            }
        }
    }

    //agregar un nuevo juego
    fun agregarJuego(juego: Juego) {
        viewModelScope.launch {
            try {
                firestoreManager.addJuego(juego)
            } catch (e: Exception) {
                Log.e("JuegosViewModel", "Error al agregar juego", e)
            }
        }
    }

}
