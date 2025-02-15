package com.example.videojuegosapi.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.videojuegosapi.data.FirestoreManager
import com.example.videojuegosapi.ui.screens.Consolas.ConsolasViewModelFactory
import com.example.videojuegosapi.ui.screens.DetalleJuegos.DetalleJuegosViewModel

@Composable
fun JuegoDetallesScreen(
    navController: NavController,
    NombreJuego: String?,
    firestoreManager: FirestoreManager,
    viewModel: DetalleJuegosViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = DetalleJuegosViewModel(firestoreManager)
    )
) {
    val detalleJuego by viewModel.detalleJuego.collectAsState()

    // Cargar los detalles del juego al iniciar
    LaunchedEffect(NombreJuego) {
        Log.d("JuegoDetalles", "Nombre del juego: $NombreJuego")
        NombreJuego?.let {
            viewModel.cargarDetalleJuego(it)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEFF7FF))
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
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

            detalleJuego?.let { juego ->
                // Imagen del juego
                AsyncImage(
                    model = juego.imagen,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shadow(8.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = juego.nombre,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = juego.descripcion,
                    fontSize = 14.sp,
                    color = Color(0xFF37474F),
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Consolas disponibles:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                juego.consolas.forEach { consola ->
                    Text(
                        text = "- $consola",
                        fontSize = 14.sp,
                        color = Color(0xFF607D8B),
                        modifier = Modifier.padding(start = 16.dp, bottom = 4.dp)
                    )
                }
            }
        }
    }
}
