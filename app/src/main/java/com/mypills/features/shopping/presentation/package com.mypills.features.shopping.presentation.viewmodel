package com.mypills.features.shopping.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mypills.features.shopping.domain.model.*
import com.mypills.features.shopping.domain.usecase.*

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val getActiveShoppingListsUseCase: GetActiveShoppingListsUseCase,
    private val createShoppingListUseCase: CreateShoppingListUseCase,
    private val addItemToListUseCase: AddItemToListUseCase,
    private val optimizeShoppingListUseCase: OptimizeShoppingListUseCase,
    private val updateItemPriceUseCase: UpdateItemPriceUseCase,
    private val getPriceInsightsUseCase: GetPriceInsightsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    init {
        loadShoppingLists()
    }

    private fun loadShoppingLists() {
        viewModelScope.launch {
            getActiveShoppingListsUseCase().collect { lists ->
                _uiState.value = _uiState.value.copy(
                    shoppingLists = lists,
                    isLoading = false
                )
            }
        }
    }

    fun createShoppingList(name: String, budget: Double?) {
        viewModelScope.launch {
            try {
                val list = createShoppingListUseCase(name, budget)
                _uiState.value = _uiState.value.copy(
                    message = "Lista criada com sucesso!",
                    selectedListId = list.id
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao criar lista: ${e.message}"
                )
            }
        }
    }

    fun addItemToList(
        listId: String,
        name: String,
        category: String,
        quantity: Double = 1.0,
        unit: String = "un",
        estimatedPrice: Double? = null,
        barcode: String? = null
    ) {
        viewModelScope.launch {
            try {
                addItemToListUseCase(listId, name, category, quantity, unit, estimatedPrice, barcode)
                _uiState.value = _uiState.value.copy(
                    message = "Item adicionado à lista!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao adicionar item: ${e.message}"
                )
            }
        }
    }

    fun optimizeList(listId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isOptimizing = true)
            try {
                val analysis = optimizeShoppingListUseCase(listId)
                _uiState.value = _uiState.value.copy(
                    budgetAnalysis = analysis,
                    isOptimizing = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao otimizar lista: ${e.message}",
                    isOptimizing = false
                )
            }
        }
    }

    fun updateItemPrice(itemId: String, actualPrice: Double, store: String? = null) {
        viewModelScope.launch {
            try {
                updateItemPriceUseCase(itemId, actualPrice, store)
                _uiState.value = _uiState.value.copy(
                    message = "Preço atualizado!"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao atualizar preço: ${e.message}"
                )
            }
        }
    }

    fun getPriceInsights(productName: String) {
        viewModelScope.launch {
            try {
                val insights = getPriceInsightsUseCase(productName)
                _uiState.value = _uiState.value.copy(
                    priceInsights = insights
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erro ao obter insights de preço: ${e.message}"
                )
            }
        }
    }

    fun setSelectedList(listId: String) {
        _uiState.value = _uiState.value.copy(selectedListId = listId)
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}

data class ShoppingUiState(
    val shoppingLists: List<ShoppingList> = emptyList(),
    val selectedListId: String? = null,
    val budgetAnalysis: BudgetAnalysis? = null,
    val priceInsights: PriceInsight? = null,
    val isLoading: Boolean = true,
    val isOptimizing: Boolean = false,
    val error: String? = null,
    val message: String? = null
)
