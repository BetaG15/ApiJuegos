package com.example.videojuegosapi.ui.screens.ListaJuegos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.videojuegosapi.data.FirestoreManager

class JuegosViewModelFactory(private val firestoreManager: FirestoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JuegosViewModel::class.java)) {
            return JuegosViewModel(firestoreManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
