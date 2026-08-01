package com.devhjs.plantdex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.devhjs.plantdex.presentation.analyze.AnalyzeScreenRoot
import com.devhjs.plantdex.presentation.designsystem.AppColors
import com.devhjs.plantdex.presentation.designsystem.PlantDexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlantDexTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = AppColors.Cream,
                ) { innerPadding ->
                    AnalyzeScreenRoot(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}