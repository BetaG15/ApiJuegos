package com.example.videojuegosapi.data.network

import com.example.videojuegosapi.data.objetos.DetalleJuegos
import com.example.videojuegosapi.data.objetos.Juego
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

object ApiClient {
    //URL base de la API, separada de la key para poder acceder a los datos de la API
    private const val BASE_URL = "https://api.rawg.io/api"
    private const val API_KEY = "a7467b6b33b14876bb69122cbc080a1c"

    //Ya que mi API se divide en páginas, necesito cargar varias páginas para obtener todos los juegos, por lo que he decidido leer solo las 3 primeras
    //Funcion suspend, es decir, puede pausar su ejecución y reanudarla más tarde
    suspend fun getGames(): List<Juego> = withContext(Dispatchers.IO) {
        //Lista mutable de juegos
        val juegos = mutableListOf<Juego>()
        val paginasCargar = 3 // Número de páginas a cargar inicialmente

        for (page in 1..paginasCargar) {
            //Obtener la respuesta de la API convirtiéndola en un JSON y extrayendo el array de juegos
            val response = URL("$BASE_URL/games?key=$API_KEY&page=$page").readText()
            val jsonObject = JSONObject(response)
            val jsonArray = jsonObject.getJSONArray("results")

            //Recorrer el array de juegos y añadirlos a la lista de juegos
            for (index in 0 until jsonArray.length()) {
                val gameObject = jsonArray.getJSONObject(index)
                juegos.add(
                    Juego(
                        id = gameObject.getString("id"),
                        nombre = gameObject.getString("name"),
                        imagenUrl = gameObject.optString("background_image", "") // Manejar si la imagen es null
                    )
                )
            }
        }

        return@withContext juegos
    }

    suspend fun getGameDetails(gameId: String): DetalleJuegos = withContext(Dispatchers.IO) {
        val response = URL("$BASE_URL/games/$gameId?key=$API_KEY").readText()
        val jsonObject = JSONObject(response)
        DetalleJuegos(
            id = jsonObject.getString("id"),
            nombre = jsonObject.getString("name"),
            imagenUrl = jsonObject.getString("background_image"),
            fecha = jsonObject.getString("released"),
            generos = jsonObject.getJSONArray("genres").let { array ->
                (0 until array.length()).map { array.getJSONObject(it).getString("name") } //Mapear los géneros a una lista de strings
            },
            descripcion = jsonObject.getString("description_raw")
        )
    }


}