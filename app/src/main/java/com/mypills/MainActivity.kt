package com.mypills

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.mypills.core.navigation.AppNavigation
import com.mypills.core.theme.MyPillsTheme
import com.mypills.features.onboarding.presentation.screen.OnboardingScreen
import com.mypills.features.onboarding.presentation.viewmodel.OnboardingViewModel

/**
 * Activity principal da aplicação My Pills
 * 
 * Responsabilidades:
 * - Gerenciar splash screen
 * - Configurar tema Material 3
 * - Decidir fluxo inicial (onboarding vs dashboard)
 * - Configurar navegação principal
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            MyPillsTheme {
                // Decidir baseado em autenticação
                AuthenticationDecisionFlow()
            }
        }
    }
}

@Composable
private fun AuthenticationDecisionFlow() {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState by authViewModel.authState.collectAsState()
    
    when (authState) {
        AuthState.CHECKING -> SplashScreen()
        AuthState.NEEDS_ONBOARDING -> OnboardingFlow()
        AuthState.NEEDS_AUTH -> LoginScreen()
        AuthState.AUTHENTICATED -> MainAppNavigation()
    }
}