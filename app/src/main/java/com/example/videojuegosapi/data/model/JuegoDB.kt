package com.example.videojuegosapi.data.model

data class JuegoDB(
    val nombre: String = "",
    val descripcion: String = "",
    val imagen: String = "",
    val consolas: List<String> = emptyList()
)