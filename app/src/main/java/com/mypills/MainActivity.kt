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
        // Instalar splash screen antes de super.onCreate()
        val splashScreen = installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        // Configurar edge-to-edge display
        enableEdgeToEdge()
        
        // Configurar conteúdo da UI
        setContent {
            MyPillsApp()
        }
        
        Timber.d("MainActivity criada")
    }
}

/**
 * Composable principal da aplicação
 * Gerencia navegação entre onboarding e app principal
 */
@Composable
private fun MyPillsApp() {
    // Aplicar tema Material 3 customizado
    MyPillsTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppContent()
        }
    }
}

/**
 * Conteúdo principal - decide entre onboarding e navegação normal
 */
@Composable
private fun AppContent() {
    val onboardingViewModel: OnboardingViewModel = hiltViewModel()
    val shouldShowOnboarding by onboardingViewModel.shouldShowOnboarding.collectAsState()
    
    // Aguardar carregamento das preferências
    if (shouldShowOnboarding == null) {
        // Tela de loading pode ser mostrada aqui
        return
    }
    
    if (shouldShowOnboarding == true) {
        // Primeira vez - mostrar onboarding
        OnboardingFlow(onboardingViewModel)
    } else {
        // Usuário já viu onboarding - ir direto para o app
        AppNavigation()
    }
}

/**
 * Fluxo de onboarding para novos usuários
 */
@Composable
private fun OnboardingFlow(viewModel: OnboardingViewModel) {
    OnboardingScreen(
        onOnboardingComplete = {
            viewModel.completeOnboarding()
        }
    )
    
    // Observar quando onboarding for completado
    val isOnboardingCompleted by viewModel.isOnboardingCompleted.collectAsState()
    
    LaunchedEffect(isOnboardingCompleted) {
        if (isOnboardingCompleted) {
            // Navegar para app principal
            // A recomposição irá mostrar AppNavigation()
        }
    }
}