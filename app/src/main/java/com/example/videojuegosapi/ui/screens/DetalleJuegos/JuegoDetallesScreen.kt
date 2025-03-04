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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.videojuegosapi.data.model.Juego
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
    var showEditForm by remember { mutableStateOf(false) }

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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
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

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de eliminar y actualizar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            viewModel.eliminarJuego(NombreJuego!!)
                            navController.popBackStack()
                                  },
                        colors = ButtonDefaults.buttonColors(Color.Red)
                    ) {
                        Text("Eliminar", color = Color.White)
                    }

                    Button(
                        onClick = { showEditForm = true },
                        colors = ButtonDefaults.buttonColors(Color.Blue)
                    ) {
                        Text("Actualizar", color = Color.White)
                    }
                }
            }
        }
    }

    if (showEditForm && detalleJuego != null) {
        EditarJuegoForm(
            juego = detalleJuego!!, // Pasamos el juego actual
            firestoreManager = firestoreManager,
            onDismiss = { showEditForm = false },
            onSubmit = { nombre, descripcion, imagen, consolas ->
                viewModel.actualizarJuego(nombre, descripcion, imagen, consolas)
                viewModel.cargarDetalleJuego(nombre)
                showEditForm = false
            }
        )
    }
}

@Composable
fun EditarJuegoForm(
    juego: Juego,
    firestoreManager: FirestoreManager,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, List<String>) -> Unit
) {
    var nombre by remember { mutableStateOf(juego.nombre) }
    var descripcion by remember { mutableStateOf(juego.descripcion) }
    var imagen by remember { mutableStateOf(juego.imagen) }
    var consolasSeleccionadas by remember { mutableStateOf(juego.consolas.toMutableSet()) }
    var expanded by remember { mutableStateOf(false) }
    var listaConsolas = remember { mutableStateListOf<String>() }

    // Cargar lista de consolas disponibles desde Firestore
    LaunchedEffect(Unit) {
        firestoreManager.getConsolas().collect { consolas ->
            listaConsolas.clear()
            listaConsolas.addAll(consolas.map { it.nombre })
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Actualizar Juego") },
        text = {
            Column {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Juego") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = false
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = imagen,
                    onValueChange = { imagen = it },
                    label = { Text("URL de la Imagen") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Dropdown de consolas
                Box {
                    Button(onClick = { expanded = true }) {
                        Text("Seleccionar Consolas")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listaConsolas.forEach { consola ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        if (consolasSeleccionadas.contains(consola)) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Seleccionado",
                                                tint = Color.Green
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }
                                        Text(consola)
                                    }
                                },
                                onClick = {
                                    if (consolasSeleccionadas.contains(consola)) {
                                        consolasSeleccionadas.remove(consola)
                                    } else {
                                        consolasSeleccionadas.add(consola)

                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text("Consolas Seleccionadas: ${consolasSeleccionadas.joinToString(", ")}")
            }
        },
        confirmButton = {
            Button(onClick = {
                if (nombre.isNotBlank() && descripcion.isNotBlank() && imagen.isNotBlank()) {
                    onSubmit(nombre, descripcion, imagen, consolasSeleccionadas.toList())
                }
            }) {
                Text("Actualizar Juego")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

