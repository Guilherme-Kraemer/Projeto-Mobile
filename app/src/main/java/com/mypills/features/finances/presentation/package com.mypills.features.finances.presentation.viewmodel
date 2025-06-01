package com.mypills.features.finances.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import javax.inject.Inject
import com.mypills.features.finances.domain.model.*
import com.mypills.features.finances.domain.usecase.*

@HiltViewModel
class FinancesViewModel @Inject constructor(
    private val getFinancialSummaryUseCase: GetFinancialSummaryUseCase,
    private val getAllAccountsUseCase: GetAllAccountsUseCase,
    private val getTransactionsBetweenDatesUseCase: GetTransactionsBetweenDatesUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val calculateCompoundInterestUseCase: CalculateCompoundInterestUseCase,
    private val calculateLoanPaymentUseCase: CalculateLoanPaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FinancesUiState())
    val uiState: StateFlow<FinancesUiState> = _uiState.asStateFlow()

    private val currentDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    private val startOfMonth = LocalDate(currentDate.year, currentDate.month, 1)

    init {
        loadFinancialData()
    }

    private fun loadFinancialData() {
        viewModelScope.launch {
            try {
                // Load accounts
                getAllAccountsUseCase().collect { accounts ->
                    _uiState.value = _uiState.value.copy(accounts = accounts)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }

        viewModelScope.launch {
            try {
                // Load transactions for current month
                getTransactionsBetweenDatesUseCase(startOfMonth, currentDate).collect { transactions ->
                    _uiState.value = _uiState.value.copy(
                        transactions = transactions,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }

        viewModelScope.launch {
            try {
                // Load financial summary
                val summary = getFinancialSummaryUseCase(startOfMonth, currentDate)
                _uiState.value = _uiState.value.copy(summary = summary)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun calculateCompoundInterest(principal: Double, rate: Double, years: Int) {
        val result = calculateCompoundInterestUseCase(principal, rate, years)
        _uiState.value = _uiState.value.copy(compoundInterestResult = result)
    }

    fun calculateLoanPayment(principal: Double, rate: Double, years: Int) {
        val result = calculateLoanPaymentUseCase(principal, rate, years)
        _uiState.value = _uiState.value.copy(loanCalculationResult = result)
    }

    fun addTransaction(transaction: FinancialTransaction) {
        viewModelScope.launch {
            try {
                addTransactionUseCase(transaction)
                _uiState.value = _uiState.value.copy(message = "Transação adicionada com sucesso!")
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = "Erro ao adicionar transação: ${e.message}")
            }
        }
    }

    fun setSelectedTab(tab: FinanceTab) {
        _uiState.value = _uiState.value.copy(selectedTab = tab)
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null, error = null)
    }
}

data class FinancesUiState(
    val accounts: List<FinancialAccount> = emptyList(),
    val transactions: List<FinancialTransaction> = emptyList(),
    val summary: FinancialSummary? = null,
    val compoundInterestResult: CompoundInterestCalculation? = null,
    val loanCalculationResult: LoanCalculation? = null,
    val selectedTab: FinanceTab = FinanceTab.OVERVIEW,
    val isLoading: Boolean = true,
    val error: String? = null,
    val message: String? = null
)

enum class FinanceTab {
    OVERVIEW, TRANSACTIONS, CALCULATORS, REPORTS
}
