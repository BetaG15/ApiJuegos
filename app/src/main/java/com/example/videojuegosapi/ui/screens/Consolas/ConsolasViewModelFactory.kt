//Esta clase se utiliza cuando el ViewModel necesita recibir dependencias adicionales, ya que
//por defecto ViewModel se crea usando un constructor sin parametros, y necesito un constructor
//CON parametros para usar ek FirestoreManager
package com.example.videojuegosapi.ui.screens.Consolas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.videojuegosapi.data.FirestoreManager

//Clase que extiende de factory
class ConsolasViewModelFactory(
    private val firestoreManager: FirestoreManager
) : ViewModelProvider.Factory {

    //Metodo obligatorio que me permite crear ViewModel con parametro
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConsolasViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConsolasViewModel(firestoreManager) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}