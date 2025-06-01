package com.mypills.core.utils

object ValidationUtils {
    
    fun validateMedicationName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("Nome é obrigatório")
            name.length < 2 -> ValidationResult.Error("Nome deve ter pelo menos 2 caracteres")
            name.length > 100 -> ValidationResult.Error("Nome muito longo")
            else -> ValidationResult.Success
        }
    }
    
    fun validateDosage(dosage: String): ValidationResult {
        return when {
            dosage.isBlank() -> ValidationResult.Error("Dosagem é obrigatória")
            dosage.toDoubleOrNull() == null -> ValidationResult.Error("Dosagem deve ser um número")
            dosage.toDouble() <= 0 -> ValidationResult.Error("Dosagem deve ser maior que zero")
            else -> ValidationResult.Success
        }
    }
    
    fun validateQuantity(quantity: String): ValidationResult {
        return when {
            quantity.isBlank() -> ValidationResult.Error("Quantidade é obrigatória")
            quantity.toIntOrNull() == null -> ValidationResult.Error("Quantidade deve ser um número inteiro")
            quantity.toInt() < 0 -> ValidationResult.Error("Quantidade não pode ser negativa")
            else -> ValidationResult.Success
        }
    }
    
    fun validatePrice(price: String): ValidationResult {
        if (price.isBlank()) return ValidationResult.Success // Price is optional
        
        return when {
            price.toDoubleOrNull() == null -> ValidationResult.Error("Preço deve ser um número")
            price.toDouble() < 0 -> ValidationResult.Error("Preço não pode ser negativo")
            price.toDouble() > 999999 -> ValidationResult.Error("Preço muito alto")
            else -> ValidationResult.Success
        }
    }
    
    fun validateTransactionAmount(amount: String): ValidationResult {
        return when {
            amount.isBlank() -> ValidationResult.Error("Valor é obrigatório")
            amount.toDoubleOrNull() == null -> ValidationResult.Error("Valor deve ser um número")
            amount.toDouble() <= 0 -> ValidationResult.Error("Valor deve ser maior que zero")
            else -> ValidationResult.Success
        }
    }
    
    fun validateBudget(budget: String): ValidationResult {
        if (budget.isBlank()) return ValidationResult.Success // Budget is optional
        
        return when {
            budget.toDoubleOrNull() == null -> ValidationResult.Error("Orçamento deve ser um número")
            budget.toDouble() <= 0 -> ValidationResult.Error("Orçamento deve ser maior que zero")
            else -> ValidationResult.Success
        }
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}
