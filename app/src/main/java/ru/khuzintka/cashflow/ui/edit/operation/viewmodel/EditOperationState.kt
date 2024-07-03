package ru.khuzintka.cashflow.ui.edit.operation.viewmodel


data class EditOperationState(
    val accountName: String,
    val count: Long,
    val desc: String,
    val isIncome: Boolean,
)