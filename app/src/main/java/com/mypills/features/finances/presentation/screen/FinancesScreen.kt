package com.mypills.features.finances.presentation.screen

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
import com.mypills.features.finances.presentation.viewmodel.FinancesViewModel
import com.mypills.features.finances.presentation.viewmodel.FinanceTab
import com.mypills.core.theme.ModuleCard
import com.mypills.core.theme.StatusChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancesScreen(
    navController: NavController,
    viewModel: FinancesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
                text = "Finanças",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            
            IconButton(onClick = { /* Add transaction */ }) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar Transação")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabs
        TabRow(selectedTabIndex = uiState.selectedTab.ordinal) {
            FinanceTab.values().forEach { tab ->
                Tab(
                    selected = uiState.selectedTab == tab,
                    onClick = { viewModel.setSelectedTab(tab) },
                    text = {
                        Text(
                            text = when (tab) {
                                FinanceTab.OVERVIEW -> "Visão Geral"
                                FinanceTab.TRANSACTIONS -> "Transações"
                                FinanceTab.CALCULATORS -> "Calculadoras"
                                FinanceTab.REPORTS -> "Relatórios"
                            }
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Content based on selected tab
        when (uiState.selectedTab) {
            FinanceTab.OVERVIEW -> OverviewContent(uiState = uiState)
            FinanceTab.TRANSACTIONS -> TransactionsContent(uiState = uiState)
            FinanceTab.CALCULATORS -> CalculatorsContent(viewModel = viewModel, uiState = uiState)
            FinanceTab.REPORTS -> ReportsContent(uiState = uiState)
        }
    }
}

@Composable
private fun OverviewContent(uiState: FinancesUiState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Financial Summary
        item {
            uiState.summary?.let { summary ->
                FinancialSummaryCard(summary = summary)
            }
        }

        // Accounts
        item {
            AccountsSection(accounts = uiState.accounts)
        }

        // Recent Transactions
        item {
            RecentTransactionsSection(transactions = uiState.transactions.take(5))
        }
    }
}

@Composable
private fun FinancialSummaryCard(summary: FinancialSummary) {
    ModuleCard(module = "finances") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Resumo Financeiro",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FinancialMetric(
                    label = "Receitas",
                    value = "R$ %.2f".format(summary.totalIncome),
                    color = MaterialTheme.colorScheme.primary
                )
                
                FinancialMetric(
                    label = "Despesas",
                    value = "R$ %.2f".format(summary.totalExpenses),
                    color = MaterialTheme.colorScheme.error
                )
                
                FinancialMetric(
                    label = "Saldo",
                    value = "R$ %.2f".format(summary.balance),
                    color = if (summary.balance >= 0) MaterialTheme.colorScheme.primary 
                           else MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Medication expenses highlight
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.Medication,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Gastos com Medicamentos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "R$ %.2f".format(summary.medicationExpenses),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FinancialMetric(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun AccountsSection(accounts: List<FinancialAccount>) {
    ModuleCard(module = "finances") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Contas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            accounts.forEach { account ->
                AccountItem(account = account)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun AccountItem(account: FinancialAccount) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                when (account.type) {
                    AccountType.CHECKING -> Icons.Filled.AccountBalance
                    AccountType.SAVINGS -> Icons.Filled.Savings
                    AccountType.CREDIT_CARD -> Icons.Filled.CreditCard
                    AccountType.CASH -> Icons.Filled.Payments
                    AccountType.INVESTMENT -> Icons.Filled.TrendingUp
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = account.type.name.lowercase().replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Text(
            text = account.formattedBalance,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = if (account.balance >= 0) MaterialTheme.colorScheme.primary 
                   else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun RecentTransactionsSection(transactions: List<FinancialTransaction>) {
    ModuleCard(module = "finances") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Transações Recentes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (transactions.isEmpty()) {
                Text(
                    text = "Nenhuma transação este mês",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                transactions.forEach { transaction ->
                    TransactionItem(transaction = transaction)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TransactionItem(transaction: FinancialTransaction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                when (transaction.type) {
                    TransactionType.INCOME -> Icons.Filled.TrendingUp
                    TransactionType.EXPENSE -> Icons.Filled.TrendingDown
                    TransactionType.TRANSFER -> Icons.Filled.SwapHoriz
                },
                contentDescription = null,
                tint = when (transaction.type) {
                    TransactionType.INCOME -> MaterialTheme.colorScheme.primary
                    TransactionType.EXPENSE -> MaterialTheme.colorScheme.error
                    TransactionType.TRANSFER -> MaterialTheme.colorScheme.outline
                }
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = transaction.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${transaction.category} • ${transaction.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = transaction.formattedAmount,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = when (transaction.type) {
                    TransactionType.INCOME -> MaterialTheme.colorScheme.primary
                    TransactionType.EXPENSE -> MaterialTheme.colorScheme.error
                    TransactionType.TRANSFER -> MaterialTheme.colorScheme.outline
                }
            )
            
            if (transaction.medicationRelated) {
                StatusChip("Medicamento", "info")
            }
        }
    }
}

@Composable
private fun TransactionsContent(uiState: FinancesUiState) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uiState.transactions) { transaction ->
            Card {
                TransactionItem(transaction = transaction)
            }
        }
    }
}

@Composable
private fun CalculatorsContent(
    viewModel: FinancesViewModel,
    uiState: FinancesUiState
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            CompoundInterestCalculator(
                onCalculate = { principal, rate, years ->
                    viewModel.calculateCompoundInterest(principal, rate, years)
                },
                result = uiState.compoundInterestResult
            )
        }
        
        item {
            LoanCalculator(
                onCalculate = { principal, rate, years ->
                    viewModel.calculateLoanPayment(principal, rate, years)
                },
                result = uiState.loanCalculationResult
            )
        }
    }
}

@Composable
private fun CompoundInterestCalculator(
    onCalculate: (Double, Double, Int) -> Unit,
    result: CompoundInterestCalculation?
) {
    ModuleCard(module = "finances") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Calculadora de Juros Compostos",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            var principal by remember { mutableStateOf("") }
            var rate by remember { mutableStateOf("") }
            var years by remember { mutableStateOf("") }

            OutlinedTextField(
                value = principal,
                onValueChange = { principal = it },
                label = { Text("Valor Inicial (R$)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = rate,
                onValueChange = { rate = it },
                label = { Text("Taxa Anual (%)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = years,
                onValueChange = { years = it },
                label = { Text("Período (anos)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val p = principal.toDoubleOrNull()
                    val r = rate.toDoubleOrNull()
                    val y = years.toIntOrNull()
                    if (p != null && r != null && y != null) {
                        onCalculate(p, r, y)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular")
            }

            // Show result
            result?.let { calc ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Resultado",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Valor Final: R$ %.2f".format(calc.finalAmount))
                        Text("Juros Ganhos: R$ %.2f".format(calc.totalInterest))
                    }
                }
            }
        }
    }
}

@Composable
private fun LoanCalculator(
    onCalculate: (Double, Double, Int) -> Unit,
    result: LoanCalculation?
) {
    ModuleCard(module = "finances") {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Calculadora de Empréstimo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            var principal by remember { mutableStateOf("") }
            var rate by remember { mutableStateOf("") }
            var years by remember { mutableStateOf("") }

            OutlinedTextField(
                value = principal,
                onValueChange = { principal = it },
                label = { Text("Valor do Empréstimo (R$)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = rate,
                onValueChange = { rate = it },
                label = { Text("Taxa Anual (%)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = years,
                onValueChange = { years = it },
                label = { Text("Prazo (anos)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val p = principal.toDoubleOrNull()
                    val r = rate.toDoubleOrNull()
                    val y = years.toIntOrNull()
                    if (p != null && r != null && y != null) {
                        onCalculate(p, r, y)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Calcular")
            }

            // Show result
            result?.let { calc ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Resultado",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Parcela Mensal: R$ %.2f".format(calc.monthlyPayment))
                        Text("Total Pago: R$ %.2f".format(calc.totalPayment))
                        Text("Juros Pagos: R$ %.2f".format(calc.totalInterest))
                    }
                }
            }
        }
    }
}

@Composable
private fun ReportsContent(uiState: FinancesUiState) {
    Text(
        text = "Relatórios em desenvolvimento...",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}