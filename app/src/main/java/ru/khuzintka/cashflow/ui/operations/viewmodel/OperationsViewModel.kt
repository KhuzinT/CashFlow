package ru.khuzintka.cashflow.ui.operations.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.khuzintka.cashflow.data.local.model.Operation
import ru.khuzintka.cashflow.data.local.repo.OperationRepo
import ru.khuzintka.cashflow.util.SharedPreferencesManager
import ru.khuzintka.cashflow.util.toOperationFilter
import javax.inject.Inject

const val operationFilterSharPrefTag = "filter"

@HiltViewModel
class OperationsViewModel
@Inject constructor(
    private val repo: OperationRepo,
    private val sharPref: SharedPreferencesManager,
) : ViewModel() {

    private val _state = MutableStateFlow(
        OperationsState(
            items = mutableListOf(),
            filter = OperationFilter.ALL,
            income = 0L,
            expenses = 0L,
        )
    )
    val state: StateFlow<OperationsState> = _state

    init {
        refresh()
    }

    fun event(type: OperationsEvent) {
        when (type) {
            is OperationsEvent.OnIncomeClick -> click(true)
            is OperationsEvent.OnExpensesClick -> click(false)
            is OperationsEvent.Refresh -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.all().collect { items ->
                _state.update {
                    it.copy(
                        items = items.toMutableList(),
                        filter = sharPref.getString(operationFilterSharPrefTag, "all").toOperationFilter(),
                        income = count(items, true),
                        expenses = count(items, false),
                    )
                }
            }
        }
    }

    private fun count(items: List<Operation>, isIncome: Boolean): Long {
        var res = 0L
        for (operation in items) {
            if (operation.isIncome == isIncome) {
                res += operation.count
            }
        }
        return res
    }

    private fun click(isIncome: Boolean) {
        val curr = _state.value.filter

        val filter = if (isIncome) {
            if (curr == OperationFilter.INCOME) OperationFilter.ALL else OperationFilter.INCOME
        } else {
            if (curr == OperationFilter.EXPENSES) OperationFilter.ALL else OperationFilter.EXPENSES
        }

        sharPref.saveString(operationFilterSharPrefTag, filter.tag)
        _state.update {
            it.copy(filter = filter)
        }
    }
}