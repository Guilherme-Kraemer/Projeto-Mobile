// app/src/main/java/com/mypills/Login.kt
package com.mypills

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mypills.core.theme.MyPillsTheme
import com.mypills.core.settings.AppPreferences
import com.mypills.core.security.BiometricAuthManager

@AndroidEntryPoint
class Login : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            MyPillsTheme {
                LoginScreen(
                    onLoginSuccess = {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val appPreferences: AppPreferences,
    private val biometricAuthManager: BiometricAuthManager
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    
    init {
        checkFirstTime()
        checkBiometricSettings()
    }
    
    private fun checkFirstTime() {
        viewModelScope.launch {
            appPreferences.isFirstTime.collect { isFirstTime ->
                _uiState.value = _uiState.value.copy(isFirstTime = isFirstTime)
            }
        }
    }
    
    private fun checkBiometricSettings() {
        viewModelScope.launch {
            appPreferences.biometricEnabled.collect { enabled ->
                _uiState.value = _uiState.value.copy(
                    biometricEnabled = enabled,
                    biometricAvailable = biometricAuthManager.isBiometricAvailable()
                )
            }
        }
    }
    
    fun authenticateWithBiometric(activity: ComponentActivity) {
        biometricAuthManager.authenticateWithBiometric(
            activity = activity,
            onSuccess = {
                _uiState.value = _uiState.value.copy(isAuthenticated = true)
            },
            onError = { error ->
                _uiState.value = _uiState.value.copy(error = error)
            }
        )
    }
    
    fun authenticateWithPin(pin: String) {
        // For demo purposes, accept "1234" as PIN
        if (pin == "1234") {
            _uiState.value = _uiState.value.copy(isAuthenticated = true)
        } else {
            _uiState.value = _uiState.value.copy(error = "PIN incorreto")
        }
    }
    
    fun skipAuthentication() {
        _uiState.value = _uiState.value.copy(isAuthenticated = true)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class LoginUiState(
    val isFirstTime: Boolean = true,
    val biometricEnabled: Boolean = false,
    val biometricAvailable: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var pin by remember { mutableStateOf("") }
    
    // Handle successful authentication
    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            onLoginSuccess()
        }
    }
    
    // Clear error after showing
    uiState.error?.let { error ->
        LaunchedEffect(error) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearError()
        }
    }
    
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo and Title
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Box(
                    modifier = Modifier.padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Filled.Medication,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "My Pills",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Sua saúde em suas mãos",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Authentication options
            if (uiState.isFirstTime) {
                // First time - just welcome
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Bem-vindo!",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Configure sua segurança nas configurações",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { viewModel.skipAuthentication() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Continuar")
                        }
                    }
                }
            } else {
                // Authentication required
                Card {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Autenticação Necessária",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Biometric authentication
                        if (uiState.biometricEnabled && uiState.biometricAvailable) {
                            OutlinedButton(
                                onClick = { 
                                    viewModel.authenticateWithBiometric(context as ComponentActivity)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    Icons.Filled.Fingerprint,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Usar Biometria")
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "ou",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        
                        // PIN authentication
                        OutlinedTextField(
                            value = pin,
                            onValueChange = { if (it.length <= 4) pin = it },
                            label = { Text("PIN de 4 dígitos") },
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            isError = uiState.error != null
                        )
                        
                        uiState.error?.let { error ->
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(
                            onClick = { viewModel.authenticateWithPin(pin) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = pin.length == 4
                        ) {
                            Text("Entrar")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Features preview
            Text(
                text = "Funcionalidades",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeatureIcon(
                    icon = Icons.Filled.Medication,
                    label = "Medicamentos",
                    color = Color(0xFF4CAF50)
                )
                FeatureIcon(
                    icon = Icons.Filled.AttachMoney,
                    label = "Finanças",
                    color = Color(0xFFFF9800)
                )
                FeatureIcon(
                    icon = Icons.Filled.DirectionsBus,
                    label = "Transporte",
                    color = Color(0xFF2196F3)
                )
                FeatureIcon(
                    icon = Icons.Filled.SmartToy,
                    label = "IA",
                    color = Color(0xFF9C27B0)
                )
            }
        }
    }
}

@Composable
private fun FeatureIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            color = color.copy(alpha = 0.1f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(8.dp),
                tint = color
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}