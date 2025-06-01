@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = "dashboard"
    ) {
        // Dashboard
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }
        
        // Medications
        composable("medications") {
            MedicationsScreen(navController = navController)
        }
        
        composable("add_medication") {
            AddMedicationScreen(navController = navController)
        }
        
        composable(
            "medication_detail/{medicationId}",
            arguments = listOf(navArgument("medicationId") { type = NavType.StringType })
        ) { backStackEntry ->
            val medicationId = backStackEntry.arguments?.getString("medicationId")
            MedicationDetailScreen(
                medicationId = medicationId ?: "",
                navController = navController
            )
        }
        
        // Reminders
        composable("reminders") {
            RemindersScreen(navController = navController)
        }
        
        // Finances
        composable("finances") {
            FinancesScreen(navController = navController)
        }
        
        // Transport
        composable("transport") {
            TransportScreen(navController = navController)
        }
        
        // Shopping
        composable("shopping") {
            ShoppingScreen(navController = navController)
        }
        
        // Assistant
        composable("assistant") {
            AssistantScreen(navController = navController)
        }
    }
}