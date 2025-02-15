package com.example.videojuegosapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.videojuegosapi.data.AuthManager
import com.example.videojuegosapi.data.FirestoreManager
import com.example.videojuegosapi.navigation.Navigation

import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContent {
            val navController = rememberNavController()
            val auth = AuthManager(applicationContext)
            val firestoreManager = FirestoreManager()

            Navigation(
                navController = navController,
                auth = auth,
                firestoreManager = firestoreManager
            )
        }
    }
}
