package com.example.videojuegosapi.ui.screens.Consolas

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videojuegosapi.data.FirestoreManager
import com.example.videojuegosapi.data.model.Consola
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConsolasViewModel(private val firestoreManager: FirestoreManager) : ViewModel() {

    private val _consolas = MutableStateFlow<List<Consola>>(emptyList())
    val consolas: StateFlow<List<Consola>> = _consolas

    fun cargarConsolas() {
        viewModelScope.launch {
            try {
                firestoreManager.getConsolas().collect { consolasList ->
                    _consolas.value = consolasList
                    Log.d("ConsolasViewModel", "Consolas cargadas: $consolasList") // Agrega este Log
                }
            } catch (e: Exception) {
                Log.e("ConsolasViewModel", "Error al cargar las consolas: ${e.message}")
            }
        }
    }


    // Agregar una nueva consola
    fun addConsola(consola: Consola) {
        viewModelScope.launch {
            try {
                firestoreManager.addConsola(consola)
                cargarConsolas()
            } catch (e: Exception) {
                println("Error al agregar la consola: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}