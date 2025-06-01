package com.mypills.features.transport.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mypills.features.transport.domain.model.*
import com.mypills.features.transport.domain.usecase.*

@HiltViewModel
class TransportViewModel @Inject constructor(
    private val getNearbyStopsUseCase: GetNearbyStopsUseCase,
    private val getRealTimeArrivalsUseCase: GetRealTimeArrivalsUseCase,
    private val planJourneyUseCase: PlanJourneyUseCase,
    private val searchStopsUseCase: SearchStopsUseCase,
    private val getFavoriteRoutesUseCase: GetFavoriteRoutesUseCase,
    private val addFavoriteRouteUseCase: AddFavoriteRouteUseCase,
    private val getAllRoutesUseCase: GetAllRoutesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransportUiState())
    val uiState: StateFlow<TransportUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            getAllRoutesUseCase().collect { routes ->
                _uiState.value = _uiState.value.copy(
                    routes = routes,
                    isLoading = false
                )
            }
        }

        viewModelScope.launch {
            getFavoriteRoutesUseCase().collect { favorites ->
                _uiState.value = _uiState.value.copy(favorites = favorites)
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isNotBlank()) {
            searchStops(query)
        } else {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
        }
    }

    private fun searchStops(query: String) {
        viewModelScope.launch {
            searchStopsUseCase(query).collect { stops ->
                _uiState.value = _uiState.value.copy(searchResults = stops)
            }
        }
    }

    fun loadNearbyStops(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val stops = getNearbyStopsUseCase(latitude, longitude)
                _uiState.value = _uiState.value.copy(
                    nearbyStops = stops,
                    currentLocation = Pair(latitude, longitude)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao carregar paradas próximas: ${e.message}"
                )
            }
        }
    }

    fun loadArrivals(stopId: String) {
        viewModelScope.launch {
            try {
                val arrivals = getRealTimeArrivalsUseCase(stopId)
                _uiState.value = _uiState.value.copy(arrivals = arrivals)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao carregar horários: ${e.message}"
                )
            }
        }
    }

    fun planJourney(fromStopId: String, toStopId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingJourney = true)
            try {
                val journey = planJourneyUseCase(fromStopId, toStopId)
                _uiState.value = _uiState.value.copy(
                    plannedJourney = journey,
                    isLoadingJourney = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao planejar viagem: ${e.message}",
                    isLoadingJourney = false
                )
            }
        }
    }

    fun addToFavorites(favorite: FavoriteRoute) {
        viewModelScope.launch {
            try {
                addFavoriteRouteUseCase(favorite)
                _uiState.value = _uiState.value.copy(
                    message = "Rota adicionada aos favoritos!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao adicionar favorito: ${e.message}"
                )
            }
        }
    }

    fun setSelectedTab(tab: TransportTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}

data class TransportUiState(
    val routes: List<BusRoute> = emptyList(),
    val nearbyStops: List<BusStop> = emptyList(),
    val searchResults: List<BusStop> = emptyList(),
    val arrivals: List<BusArrival> = emptyList(),
    val favorites: List<FavoriteRoute> = emptyList(),
    val plannedJourney: Journey? = null,
    val selectedTab: TransportTab = TransportTab.NEARBY,
    val currentLocation: Pair<Double, Double>? = null,
    val isLoading: Boolean = true,
    val isLoadingJourney: Boolean = false,
    val error: String? = null,
    val message: String? = null
)

enum class TransportTab {
    NEARBY, ROUTES, FAVORITES, PLANNER
}
