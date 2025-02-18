package com.example.videojuegosapi.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.example.videojuegosapi.data.model.Consola
import com.example.videojuegosapi.data.model.ConsolaDB
import com.example.videojuegosapi.data.model.Juego
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class FirestoreManager {

    private val firestore = FirebaseFirestore.getInstance()

    companion object {
        private const val COLLECTION_CONSOLAS = "Consolas"
        private const val COLLECTION_JUEGOS = "Juegos"
    }

    // Obtener todas las consolas
    fun getConsolas(): Flow<List<Consola>> {
        return firestore.collection(COLLECTION_CONSOLAS)
            .snapshots()
            .map { querySnapshot ->
                val lista = querySnapshot.documents.mapNotNull { document ->
                    Consola(
                        id = document.id,
                        nombre = document.getString("nombre") ?: "",
                        imagen = document.getString("imagen") ?: ""
                    )
                }
                Log.d("FirestoreManager", "Consolas obtenidas: $lista") // Agrega este Log
                lista
            }
    }

    // Agregar una consola
    suspend fun addConsola(consola: Consola) {
        val consolaDB = ConsolaDB(
            nombre = consola.nombre,
            imagen = consola.imagen
        )
        firestore.collection(COLLECTION_CONSOLAS).add(consolaDB).await()
    }

    // Juegos filtrados por consolas
    fun getJuegos(nombreConsola: String): Flow<List<Juego>> {
        return flow {
            try {
                val querySnapshot = firestore.collection(COLLECTION_JUEGOS)
                    .whereArrayContains("consolas", nombreConsola)
                    .get()
                    .await()

                // Mapeamos los documentos a objetos Juego
                val juegosList = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(Juego::class.java)
                }

                emit(juegosList)
            } catch (e: Exception) {
                println("Error al obtener juegos: ${e.message}")
                emit(emptyList())
            }
        }
    }

    // Detalles del juego por su nombre
    suspend fun getJuegoPorNombre(nombreJuego: String): Juego? {
        return try {
            val juegosRef = firestore.collection(COLLECTION_JUEGOS)
            val querySnapshot = juegosRef.whereEqualTo("nombre", nombreJuego).get().await()
            val juego = querySnapshot.documents.firstOrNull()?.toObject(Juego::class.java)
            juego
        } catch (e: Exception) {
            Log.e("FirestoreManager", "Error al obtener el juego: ${e.message}")
            null
        }
    }

    // Agregar un nuevo juego
    suspend fun addJuego(juego: Juego) {
        val juegoRef = firestore.collection(COLLECTION_JUEGOS).document()
        juego.id = juegoRef.id
        juegoRef.set(juego).await()
    }

    // Actualizar un juego
    suspend fun actualizarJuegoPorNombre(nombreJuego: String, nuevosDatos: Map<String, Any>) {
        val juegosRef = firestore.collection(COLLECTION_JUEGOS)

        val querySnapshot = juegosRef.whereEqualTo("nombre", nombreJuego).get().await()

        if (!querySnapshot.isEmpty) {
            val document = querySnapshot.documents.first()
            document.reference.update(nuevosDatos).await()
        } else {
            Log.e("FirestoreManager", "Juego con nombre $nombreJuego no encontrado.")
        }
    }


    // Eliminar un juego por su nombre
    suspend fun deleteJuegoPorNombre(nombreJuego: String) {
        val querySnapshot = firestore.collection(COLLECTION_JUEGOS)
            .whereEqualTo("nombre", nombreJuego)
            .get()
            .await()

        for (document in querySnapshot.documents) {
            firestore.collection(COLLECTION_JUEGOS).document(document.id).delete().await()
        }
    }

}
