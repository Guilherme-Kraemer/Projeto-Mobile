package com.mypills.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Color scheme personalizado para o app
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50), // Verde medicinal
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF1B5E20),
    onPrimaryContainer = Color(0xFFC8E6C9),
    
    secondary = Color(0xFF2196F3), // Azul para transporte
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF0D47A1),
    onSecondaryContainer = Color(0xFFBBDEFB),
    
    tertiary = Color(0xFFFF9800), // Laranja para finanças
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFE65100),
    onTertiaryContainer = Color(0xFFFFE0B2),
    
    error = Color(0xFFEF5350),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFB71C1C),
    onErrorContainer = Color(0xFFFFCDD2),
    
    background = Color(0xFF121212),
    onBackground = Color(0xFFE1E1E1),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE1E1E1),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFCACACA),
    
    outline = Color(0xFF525252),
    outlineVariant = Color(0xFF404040),
    scrim = Color(0xFF000000)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D32), // Verde medicinal
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFC8E6C9),
    onPrimaryContainer = Color(0xFF1B5E20),
    
    secondary = Color(0xFF1976D2), // Azul para transporte
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFBBDEFB),
    onSecondaryContainer = Color(0xFF0D47A1),
    
    tertiary = Color(0xFFE65100), // Laranja para finanças
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE0B2),
    onTertiaryContainer = Color(0xFFBF360C),
    
    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFFB71C1C),
    
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1C1C1C),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1C1C),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF424242),
    
    outline = Color(0xFF757575),
    outlineVariant = Color(0xFFBDBDBD),
    scrim = Color(0xFF000000)
)

// Typography personalizada
private val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp,
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 45.sp,
        lineHeight = 52.sp,
        letterSpacing = 0.sp,
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = 0.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.15.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.1.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
    )
)

// Cores específicas do app
object AppColors {
    // Cores por módulo
    val MedicationPrimary = Color(0xFF4CAF50)
    val MedicationSecondary = Color(0xFF81C784)
    
    val FinancePrimary = Color(0xFFFF9800)
    val FinanceSecondary = Color(0xFFFFB74D)
    
    val TransportPrimary = Color(0xFF2196F3)
    val TransportSecondary = Color(0xFF64B5F6)
    
    val ShoppingPrimary = Color(0xFF9C27B0)
    val ShoppingSecondary = Color(0xFFBA68C8)
    
    val AssistantPrimary = Color(0xFF607D8B)
    val AssistantSecondary = Color(0xFF90A4AE)
    
    val ReminderPrimary = Color(0xFFE91E63)
    val ReminderSecondary = Color(0xFFF06292)
    
    // Status colors
    val SuccessColor = Color(0xFF4CAF50)
    val WarningColor = Color(0xFFFF9800)
    val ErrorColor = Color(0xFFF44336)
    val InfoColor = Color(0xFF2196F3)
    
    // Gradient colors
    val PrimaryGradient = listOf(
        Color(0xFF4CAF50),
        Color(0xFF66BB6A)
    )
    
    val SecondaryGradient = listOf(
        Color(0xFF2196F3),
        Color(0xFF42A5F5)
    )
}

@Composable
fun MyPillsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S -> {
            val context = androidx.compose.ui.platform.LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

// Extensions úteis para cores
@Composable
fun getModuleColor(module: String): Color {
    return when (module.lowercase()) {
        "medications", "medication" -> AppColors.MedicationPrimary
        "finances", "finance" -> AppColors.FinancePrimary
        "transport" -> AppColors.TransportPrimary
        "shopping" -> AppColors.ShoppingPrimary
        "assistant" -> AppColors.AssistantPrimary
        "reminders", "reminder" -> AppColors.ReminderPrimary
        else -> MaterialTheme.colorScheme.primary
    }
}

// Componentes customizados para o tema
@Composable
fun ModuleCard(
    module: String,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = getModuleColor(module).copy(alpha = 0.1f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, 
            getModuleColor(module).copy(alpha = 0.3f)
        )
    ) {
        content()
    }
}

@Composable
fun StatusChip(
    text: String,
    status: String, // success, warning, error, info
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    val backgroundColor = when (status) {
        "success" -> AppColors.SuccessColor
        "warning" -> AppColors.WarningColor
        "error" -> AppColors.ErrorColor
        "info" -> AppColors.InfoColor
        else -> MaterialTheme.colorScheme.primary
    }.copy(alpha = 0.1f)
    
    val textColor = when (status) {
        "success" -> AppColors.SuccessColor
        "warning" -> AppColors.WarningColor
        "error" -> AppColors.ErrorColor
        "info" -> AppColors.InfoColor
        else -> MaterialTheme.colorScheme.primary
    }
    
    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            modifier = androidx.compose.ui.Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )
        )
    }
}