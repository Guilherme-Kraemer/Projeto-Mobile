package com.mypills

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import com.mypills.core.theme.MyPillsTheme
import com.mypills.core.navigation.MyPillsNavigation

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // SplashScreen API
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // Edge-to-edge display
        enableEdgeToEdge()
        
        setContent {
            MyPillsTheme {
                MyPillsApp()
            }
        }
    }
}

@Composable
private fun MyPillsApp() {
    MyPillsNavigation()
}