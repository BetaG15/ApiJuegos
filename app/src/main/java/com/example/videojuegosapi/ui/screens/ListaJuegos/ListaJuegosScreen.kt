package com.example.videojuegosapi.ui.screens.ListaJuegos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import coil.compose.AsyncImage
import com.example.videojuegosapi.data.AuthManager
import com.example.videojuegosapi.data.FirestoreManager
import com.example.videojuegosapi.data.model.Juego

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaJuegosScreen(
    navController: NavController,
    auth: AuthManager,
    navigateToLogin: () -> Unit,
    nombreConsola: String,
    viewModel: JuegosViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = JuegosViewModelFactory(FirestoreManager())
    )
) {
    val juegos by viewModel.juegos.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showFormDialog by remember { mutableStateOf(false) }
    val user = auth.getCurrentUser()

    // Para la creacion de Juegos
    var juegoNombre by remember { mutableStateOf("") }
    var juegoDescripcion by remember { mutableStateOf("") }
    var juegoImagen by remember { mutableStateOf("") }
    var juegoConsolas by remember { mutableStateOf(listOf<String>()) }

    // Cargar juegos al iniciar
    LaunchedEffect(Unit) {
        viewModel.cargarJuegos(nombreConsola)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = nombreConsola) },
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showFormDialog = true },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Juego")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFFEFF7FF))
                .padding(paddingValues)
        ) {
            if (showDialog) {
                LogoutDialog(
                    onDismiss = { showDialog = false },
                    onConfirm = {
                        auth.signOut()
                        navigateToLogin()
                        showDialog = false
                    }
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp)
            ) {
                items(juegos) { juego ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { navController.navigate("game_details/${juego.nombre}") },
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AsyncImage(
                                model = juego.imagen,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(150.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )

                            Spacer(modifier = Modifier.height(8.dp))

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

            // Creacion de juego
            if (showFormDialog) {
                CrearJuegoForm(
                    onDismiss = { showFormDialog = false },
                    onSubmit = { nombre, descripcion, imagen, consolas ->
                        val nuevoJuego = Juego(
                            nombre = nombre,
                            descripcion = descripcion,
                            imagen = imagen,
                            consolas = consolas
                        )
                        viewModel.agregarJuego(nuevoJuego)
                        showFormDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun CrearJuegoForm(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, List<String>) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }
    var consolas by remember { mutableStateOf(listOf<String>()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Nuevo Juego") },
        text = {
            Column {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del Juego") },
                    modifier = Modifier.fillMaxWidth()
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
                TextField(
                    value = consolas.joinToString(","),
                    onValueChange = { consolas = it.split(",") },
                    label = { Text("Consolas disponibles") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                if (nombre.isNotBlank() && descripcion.isNotBlank() && imagen.isNotBlank()) {
                    onSubmit(nombre, descripcion, imagen, consolas)
                }
            }) {
                Text("Crear Juego")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}


@Composable
fun LogoutDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Cerrar Sesión") },
        text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}