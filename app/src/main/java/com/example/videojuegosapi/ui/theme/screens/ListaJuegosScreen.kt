package com.example.videojuegosapi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.videojuegosapi.data.objetos.Juego
import com.example.videojuegosapi.data.network.ApiClient
import com.example.videojuegosapi.viewmodel.JuegosViewModel

@Composable
fun ListaJuegosScreen(navController: NavController, viewModel: JuegosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val juegos by viewModel.juegos.collectAsState()

    // Función encargada de cargar todos los juegos
    LaunchedEffect(Unit) {
        viewModel.cargarJuegos()
    }

    // Fondo general
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEFF7FF)) // Fondo azul claro
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(8.dp) // Espaciado general
        ) {
            items(juegos) { juego ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { navController.navigate("game_details/${juego.id}") },
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Imagen del juego con esquinas redondeadas
                        AsyncImage(
                            model = juego.imagenUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Nombre del juego
                        Text(
                            text = juego.nombre,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFF37474F),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}


