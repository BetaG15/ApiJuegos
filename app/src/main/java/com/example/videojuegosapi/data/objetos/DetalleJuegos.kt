package com.example.videojuegosapi.data.objetos

data class DetalleJuegos(
    val id: String,
    val nombre: String,
    val imagenUrl: String,
    val fecha: String,
    val generos: List<String>,
    val descripcion: String
)