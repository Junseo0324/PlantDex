package com.devhjs.plantdex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import com.devhjs.plantdex.presentation.navigation.PlantDexNavDisplay
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantDexTheme {
                PlantDexNavDisplay()
            }
        }
    }
}
