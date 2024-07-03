package ru.khuzintka.cashflow.ui.operations.viewmodel

sealed class OperationsEvent {
    object OnIncomeClick : OperationsEvent()
    object OnExpensesClick : OperationsEvent()
    object Refresh : OperationsEvent()
}