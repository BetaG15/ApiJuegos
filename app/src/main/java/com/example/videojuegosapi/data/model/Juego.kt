package com.example.videojuegosapi.data.model

data class Juego{
    var id: String? = null,
    var nombre: String? = null
    var descripcion: String? = null
    var imagen: String? = null
    var consola: Consola? = null
}