package ru.khuzintka.cashflow.ui.accounts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.khuzintka.cashflow.data.local.model.Account
import ru.khuzintka.cashflow.data.local.repo.AccountRepo
import ru.khuzintka.cashflow.util.SharedPreferencesManager
import javax.inject.Inject

const val visibilitySharPrefTag = "isVisible"

@HiltViewModel
class AccountsViewModel
@Inject constructor(
    private val repo: AccountRepo,
    private val sharPref: SharedPreferencesManager,
) : ViewModel() {

    private val _state = MutableStateFlow(
        AccountsState(
            items = mutableListOf(),
            total = 0L,
            isVisible = true,
        )
    )
    val state = _state.asStateFlow()

    init {
        refresh()
    }

    fun event(type: AccountsEvent) {
        when (type) {
            is AccountsEvent.ChangeVisibility -> changeVisibility()
            is AccountsEvent.Refresh -> refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.all().collect { items ->
                _state.update {
                    it.copy(
                        items = items.toMutableList(),
                        total = countTotal(items),
                        isVisible = sharPref.getBoolean(visibilitySharPrefTag, true)
                    )
                }
            }
        }
    }

    private fun countTotal(items: List<Account>): Long {
        var total = 0L
        for (account in items) {
            if (account.isInTotal) {
                total += account.count
            }
        }
        return total
    }

    private fun changeVisibility() {
        val isVisible = !sharPref.getBoolean(visibilitySharPrefTag, true)
        sharPref.saveBoolean(visibilitySharPrefTag, isVisible)

        _state.update {
            it.copy(isVisible = isVisible)
        }
    }
}
