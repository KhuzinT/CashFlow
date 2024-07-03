package ru.khuzintka.cashflow.ui.operations.viewmodel

import ru.khuzintka.cashflow.data.local.model.Operation

enum class OperationFilter(val tag: String) {
    ALL("all"),
    INCOME("income"),
    EXPENSES("expenses"),
}

data class OperationsState(
    val items: MutableList<Operation>,
    val filter: OperationFilter,
    val income: Long,
    val expenses: Long,
)