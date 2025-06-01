package com.mypills.features.transport.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mypills.features.transport.presentation.viewmodel.TransportViewModel
import com.mypills.features.transport.presentation.viewmodel.TransportTab
import com.mypills.core.theme.ModuleCard
import com.mypills.core.theme.StatusChip
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TransportScreen(
    navController: NavController,
    viewModel: TransportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    val locationPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(locationPermissionState.status.isGranted) {
        if (locationPermissionState.status.isGranted) {
            // Load user's current location and nearby stops
            // This would typically use LocationManager or FusedLocationProvider
            // For demo, using hardcoded coordinates (São Paulo)
            viewModel.loadNearbyStops(-23.5505, -46.6333)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Transporte",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(
                onClick = {
                    if (!locationPermissionState.status.isGranted) {
                        locationPermissionState.launchPermissionRequest()
                    }
                }
            ) {
                Icon(Icons.Filled.MyLocation, contentDescription = "Localização")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            label = { Text("Buscar paradas ou rotas") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        TabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
            TransportTab.values().forEach { tab ->
                Tab(
                    selected = uiState.selectedTab == tab,
                    onClick = { viewModel.setSelectedTab(tab) },
                    text = {
                        Text(
                            text = when (tab) {
                                TransportTab.NEARBY -> "Próximo"
                                TransportTab.ROUTES -> "Rotas"
                                TransportTab.FAVORITES -> "Favoritos"
                                TransportTab.PLANNER -> "Planejar"
                            }
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on selected tab
        when (uiState.selectedTab) {
            TransportTab.NEARBY -> NearbyContent(viewModel = viewModel, uiState = uiState)
            TransportTab.ROUTES -> RoutesContent(uiState = uiState)
            TransportTab.FAVORITES -> FavoritesContent(uiState = uiState)
            TransportTab.PLANNER -> PlannerContent(viewModel = viewModel, uiState = uiState)
        }
    }
}

@Composable
private fun NearbyContent(
    viewModel: TransportViewModel,
    uiState: TransportUiState
) {
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (uiState.nearbyStops.isNotEmpty()) {
                items(uiState.nearbyStops) { stop ->
                    BusStopCard(
                        stop = stop,
                        onStopClick = { viewModel.loadArrivals(stop.id) },
                        showDistance = true,
                        userLocation = uiState.currentLocation
                    )
                }
            } else {
                item {
                    EmptyStateCard(
                        icon = Icons.Filled.LocationOff,
                        title = "Nenhuma parada próxima",
                        subtitle = "Verifique se a localização está ativada"
                    )
                }
            }
        }
    }
}

@Composable
private fun BusStopCard(
    stop: BusStop,
    onStopClick: () -> Unit,
    showDistance: Boolean = false,
    userLocation: Pair<Double, Double>? = null
) {
    ModuleCard(module = "transport") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stop.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    stop.address?.let { address ->
                        Text(
                            text = address,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (showDistance && userLocation != null) {
                        val distance = stop.distanceTo(userLocation.first, userLocation.second)
                        Text(
                            text = "%.0f metros".format(distance * 1000),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                if (stop.hasAccessibility) {
                    Icon(
                        Icons.Filled.Accessible,
                        contentDescription = "Acessível",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(
                    onClick = { /* Show on map */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Map, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Mapa")
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Button(
                    onClick = onStopClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Schedule, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Horários")
                }
            }
        }
    }
}

@Composable
private fun RoutesContent(uiState: TransportUiState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uiState.routes) { route ->
            RouteCard(route = route)
        }
    }
}

@Composable
private fun RouteCard(route: BusRoute) {
    ModuleCard(module = "transport") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = route.routeNumber,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = route.routeName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${route.startLocation} ↔ ${route.endLocation}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                route.fare?.let { fare ->
                    Text(
                        text = "R$ %.2f".format(fare),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            route.estimatedDuration?.let { duration ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$duration min",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    route.distance?.let { distance ->
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            Icons.Filled.Straighten,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "%.1f km".format(distance),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesContent(uiState: TransportUiState) {
    if (uiState.favorites.isEmpty()) {
        EmptyStateCard(
            icon = Icons.Filled.FavoriteBorder,
            title = "Nenhuma rota favorita",
            subtitle = "Adicione suas rotas mais usadas"
        )
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.favorites) { favorite ->
                FavoriteRouteCard(favorite = favorite)
            }
        }
    }
}

@Composable
private fun FavoriteRouteCard(favorite: FavoriteRoute) {
    ModuleCard(module = "transport") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = favorite.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${favorite.routeIds.size} rota(s) disponível(is)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Row {
                IconButton(onClick = { /* Edit favorite */ }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = { /* Delete favorite */ }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Excluir")
                }
            }
        }
    }
}

@Composable
private fun PlannerContent(
    viewModel: TransportViewModel,
    uiState: TransportUiState
) {
    var fromStop by remember { mutableStateOf("") }
    var toStop by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ModuleCard(module = "transport") {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Planejar Viagem",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = fromStop,
                    onValueChange = { fromStop = it },
                    label = { Text("De (parada de origem)") },
                    leadingIcon = { Icon(Icons.Filled.MyLocation, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = toStop,
                    onValueChange = { toStop = it },
                    label = { Text("Para (parada de destino)") },
                    leadingIcon = { Icon(Icons.Filled.LocationOn, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (fromStop.isNotBlank() && toStop.isNotBlank()) {
                            viewModel.planJourney(fromStop, toStop)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isLoadingJourney && fromStop.isNotBlank() && toStop.isNotBlank()
                ) {
                    if (uiState.isLoadingJourney) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Planejar Viagem")
                    }
                }
            }
        }

        // Show planned journey
        uiState.plannedJourney?.let { journey ->
            JourneyResultCard(journey = journey)
        }
    }
}

@Composable
private fun JourneyResultCard(journey: Journey) {
    ModuleCard(module = "transport") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Resultado da Viagem",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                JourneyMetric(
                    icon = Icons.Filled.Schedule,
                    label = "Duração",
                    value = "${journey.totalDuration} min"
                )
                
                JourneyMetric(
                    icon = Icons.Filled.Straighten,
                    label = "Distância",
                    value = "%.1f km".format(journey.totalDistance)
                )
                
                JourneyMetric(
                    icon = Icons.Filled.AttachMoney,
                    label = "Custo",
                    value = "R$ %.2f".format(journey.totalFare)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Segmentos da Viagem",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            journey.routes.forEach { segment ->
                JourneySegmentItem(segment = segment)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun JourneyMetric(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun JourneySegmentItem(segment: JourneySegment) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
        ) {
            Text(
                text = segment.route.routeNumber,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "${segment.fromStop.name} → ${segment.toStop.name}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${segment.duration} min • ${segment.stops.size} paradas",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyStateCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String
) {
    ModuleCard(module = "transport") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}