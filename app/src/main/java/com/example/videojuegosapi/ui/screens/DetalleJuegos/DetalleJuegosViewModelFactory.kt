package com.example.videojuegosapi.ui.screens.DetalleJuegos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.videojuegosapi.data.FirestoreManager

class DetalleJuegosViewModelFactory(
    private val firestoreManager: FirestoreManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleJuegosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetalleJuegosViewModel(firestoreManager) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}