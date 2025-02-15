package com.example.videojuegosapi.data.model

data class Juego(
    var id: String? = null,
    val nombre: String = "",
    val imagen: String = "",
    val descripcion: String = "",
    val consolas: List<String> = emptyList()
)
