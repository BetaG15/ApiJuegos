package com.example.videojuegosapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.videojuegosapi.ui.screens.DetalleJuegos.DetalleJuegosViewModel

@Composable
fun JuegoDetallesScreen(navController: NavController, idJuego: String?, viewModel: DetalleJuegosViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val detalleJuego by viewModel.detalleJuego.collectAsState()

    //Funcion encargada de cargar los detalles del juego seleccionado por el usuario
    LaunchedEffect(idJuego) {
        //solo funciona si el id del juego no es nulo
        idJuego?.let {
            idJuego.let { viewModel.cargarDetalleJuego(it) }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEFF7FF))
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()) //Para que deje hacer scroll, por si la descripción es muy larga
                .padding(16.dp)
        ) {
            // Botón de regreso, circular y de otro tono de azul
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .align(Alignment.Start)
                    .clip(CircleShape)
                    .background(Color(0xFFB0C4DE))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            detalleJuego.let { juego ->
                // Imagen del juego con esquinas redondeadas y sombra
                AsyncImage(
                    model = juego?.imagenUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop, //Rellena el espacio completo
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Nombre del juego
                Text(
                    text = juego?.nombre ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Fecha de lanzamiento
                Text(
                    //En ingles ya que la API que elegí está en inglés, para que quede bonito
                    text = "Release Date: ${juego?.fecha}",
                    fontSize = 16.sp,
                    color = Color(0xFF607D8B) // Gris más claro
                )

                // Géneros
                Text(
                    text = "Genres: ${juego?.generos?.joinToString()}",
                    fontSize = 16.sp,
                    color = Color(0xFF607D8B),
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Descripción del juego
                Text(
                    text = juego?.descripcion ?: "",
                    fontSize = 14.sp,
                    color = Color(0xFF37474F),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

