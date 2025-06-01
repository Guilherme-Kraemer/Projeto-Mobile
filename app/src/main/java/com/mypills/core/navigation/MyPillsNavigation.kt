package com.mypills.core.navigation

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mypills.features.dashboard.presentation.screen.DashboardScreen
import com.mypills.features.medications.presentation.screen.MedicationsScreen
import com.mypills.features.finances.presentation.screen.FinancesScreen
import com.mypills.features.transport.presentation.screen.TransportScreen
import com.mypills.features.shopping.presentation.screen.ShoppingScreen
import com.mypills.features.assistant.presentation.screen.AssistantScreen
import com.mypills.features.reminders.presentation.screen.RemindersScreen

// Sealed class para as rotas
sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector = icon
) {
    object Dashboard : Screen(
        route = "dashboard",
        title = "Início",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )
    
    object Medications : Screen(
        route = "medications",
        title = "Remédios",
        icon = Icons.Outlined.Medication,
        selectedIcon = Icons.Filled.Medication
    )
    
    object Reminders : Screen(
        route = "reminders",
        title = "Lembretes",
        icon = Icons.Outlined.Notifications,
        selectedIcon = Icons.Filled.Notifications
    )
    
    object Finances : Screen(
        route = "finances",
        title = "Finanças",
        icon = Icons.Outlined.AccountBalance,
        selectedIcon = Icons.Filled.AccountBalance
    )
    
    object Transport : Screen(
        route = "transport",
        title = "Transporte",
        icon = Icons.Outlined.DirectionsBus,
        selectedIcon = Icons.Filled.DirectionsBus
    )
    
    object Shopping : Screen(
        route = "shopping",
        title = "Compras",
        icon = Icons.Outlined.ShoppingCart,
        selectedIcon = Icons.Filled.ShoppingCart
    )
    
    object Assistant : Screen(
        route = "assistant",
        title = "Assistente",
        icon = Icons.Outlined.SmartToy,
        selectedIcon = Icons.Filled.SmartToy
    )
}

// Lista de telas principais
val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.Medications,
    Screen.Reminders,
    Screen.Finances,
    Screen.Transport,
    Screen.Shopping,
    Screen.Assistant
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPillsNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { 
                        it.route == screen.route 
                    } == true
                    
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (isSelected) screen.selectedIcon else screen.icon,
                                contentDescription = screen.title
                            )
                        },
                        label = { Text(screen.title) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                slideInHorizontally(initialOffsetX = { 1000 }) + 
                fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { -1000 }) + 
                fadeOut(animationSpec = tween(300))
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -1000 }) + 
                fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { 1000 }) + 
                fadeOut(animationSpec = tween(300))
            }
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(navController = navController)
            }
            
            composable(Screen.Medications.route) {
                MedicationsScreen(navController = navController)
            }
            
            composable(Screen.Reminders.route) {
                RemindersScreen(navController = navController)
            }
            
            composable(Screen.Finances.route) {
                FinancesScreen(navController = navController)
            }
            
            composable(Screen.Transport.route) {
                TransportScreen(navController = navController)
            }
            
            composable(Screen.Shopping.route) {
                ShoppingScreen(navController = navController)
            }
            
            composable(Screen.Assistant.route) {
                AssistantScreen(navController = navController)
            }
            
            // Telas detalhadas
            composable("medication_detail/{medicationId}") { backStackEntry ->
                val medicationId = backStackEntry.arguments?.getString("medicationId")
                // MedicationDetailScreen(medicationId = medicationId, navController = navController)
            }
            
            composable("add_medication") {
                // AddMedicationScreen(navController = navController)
            }
            
            composable("finance_detail/{transactionId}") { backStackEntry ->
                val transactionId = backStackEntry.arguments?.getString("transactionId")
                // FinanceDetailScreen(transactionId = transactionId, navController = navController)
            }
            
            composable("route_planner") {
                // RoutePlannerScreen(navController = navController)
            }
            
            composable("shopping_list/{listId}") { backStackEntry ->
                val listId = backStackEntry.arguments?.getString("listId")
                // ShoppingListDetailScreen(listId = listId, navController = navController)
            }
            
            composable("chat/{conversationId}") { backStackEntry ->
                val conversationId = backStackEntry.arguments?.getString("conversationId")
                // ChatScreen(conversationId = conversationId, navController = navController)
            }
        }
    }
}