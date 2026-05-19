package com.nathanrds.caftracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.nathanrds.caftracker.di.CafTrackerApp
import com.nathanrds.caftracker.presentation.navigation.NavGraph
import com.nathanrds.caftracker.ui.theme.CafTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application
        check(app is CafTrackerApp) {
            "Application deve ser uma instância de CafTrackerApp"
        }
        val container = app.container

        setContent {
            CafTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(container = container)
                }
            }
        }
    }
}