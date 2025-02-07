package com.example.videojuegosapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.videojuegosapi.data.AuthManager
import com.example.videojuegosapi.navigation.Navigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val auth = AuthManager(applicationContext)
            Navigation(navController = navController, auth = auth)
        }
    }
}
