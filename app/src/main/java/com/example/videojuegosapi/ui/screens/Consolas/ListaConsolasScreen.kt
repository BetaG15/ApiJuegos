package com.example.videojuegosapi.ui.screens.Consolas

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.videojuegosapi.R
import com.example.videojuegosapi.data.AuthManager
import com.example.videojuegosapi.data.FirestoreManager
import com.example.videojuegosapi.data.model.Consola

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaConsolasScreen(
    navController: NavController,
    auth: AuthManager,
    firestoreManager: FirestoreManager,
    navigateToLogin: () -> Unit,
    viewModel: ConsolasViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = ConsolasViewModelFactory(firestoreManager))
) {
    val consolas by viewModel.consolas.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showAddConsolaDialog by remember { mutableStateOf(false) }
    val user = auth.getCurrentUser()

    // Cargar consolas al iniciar
    LaunchedEffect(Unit) {
        Log.d("ListaConsolasScreen", "Intentando cargar consolas...")
        viewModel.cargarConsolas()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (user?.photoUrl != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user.photoUrl)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Imagen de perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(40.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.profile),
                                contentDescription = "Foto de perfil por defecto",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Column {
                            Text(
                                text = user?.email ?: "Sin correo",
                                fontSize = 20.sp,
                                maxLines = 1,
                                color = Color.Black
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray),
                actions = {
                    IconButton(onClick = { showDialog = true }) {
                        Icon(Icons.AutoMirrored.Outlined.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddConsolaDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir consola")
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

            if (showAddConsolaDialog) {
                AddConsolaDialog(
                    onDismiss = { showAddConsolaDialog = false },
                    onConfirm = { nombreConsola, imagenConsola ->
                        viewModel.addConsola(Consola(nombre = nombreConsola, imagen = imagenConsola))
                        showAddConsolaDialog = false
                    }
                )
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(8.dp)
            ) {
                items(consolas) { consola ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { Log.d("Navigation", "Navegando a listaJuegos con nombre: ${consola.nombre}");
                                navController.navigate("listaJuegos/${consola.nombre}") },
                        shape = RoundedCornerShape(16.dp),
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Muestra la imagen de la consola
                            AsyncImage(
                                model = consola.imagen,
                                contentDescription = "Imagen de ${consola.nombre}",
                                modifier = Modifier.size(100.dp)
                            )
                            Text(
                                text = consola.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF37474F),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
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

@Composable
fun AddConsolaDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var nombreConsola by remember { mutableStateOf("") }
    var imagenConsola by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir Consola") },
        text = {
            Column {
                TextField(
                    value = nombreConsola,
                    onValueChange = { nombreConsola = it },
                    label = { Text("Nombre de la consola") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = imagenConsola,
                    onValueChange = { imagenConsola = it },
                    label = { Text("URL de la imagen") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(nombreConsola, imagenConsola) },
                enabled = nombreConsola.isNotBlank() && imagenConsola.isNotBlank()
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}