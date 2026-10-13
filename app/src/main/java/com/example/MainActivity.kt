package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ui.gameplay.GameViewModel
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.WordQuestTheme

class MainActivity : ComponentActivity() {

    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WordQuestTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(viewModel = gameViewModel)
                }
            }
        }
    }
}
