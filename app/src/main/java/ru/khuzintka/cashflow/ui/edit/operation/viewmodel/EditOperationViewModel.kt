package ru.khuzintka.cashflow.ui.edit.operation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.khuzintka.cashflow.data.local.model.Operation
import ru.khuzintka.cashflow.data.local.repo.AccountRepo
import ru.khuzintka.cashflow.data.local.repo.OperationRepo
import ru.khuzintka.cashflow.ui.accounts.accountIdArgsTag
import ru.khuzintka.cashflow.ui.edit.account.viewmodel.unknownAccountId
import javax.inject.Inject

@HiltViewModel
class EditOperationViewModel
@Inject constructor(
    private val accountRepo: AccountRepo,
    private val operationRepo: OperationRepo,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(
        EditOperationState(
            accountName = "",
            count = 0L,
            desc = "",
            isIncome = true,
        )
    )
    val state = _state.asStateFlow()

    private val accountId = savedStateHandle.getStateFlow(accountIdArgsTag, unknownAccountId).value

    init {
        viewModelScope.launch(Dispatchers.IO) {
            accountRepo.byId(accountId).collect { account ->
                _state.update {
                    it.copy(accountName = account.name)
                }
            }
        }
    }

    fun event(type: EditOperationEvent) {
        when (type) {
            is EditOperationEvent.InputCount -> inputCount(type.count)
            is EditOperationEvent.InputDesc -> inputDesc(type.desc)
            is EditOperationEvent.OnSaveClick -> save(type.warning, type.navigate)
            is EditOperationEvent.SetIncome -> changeIncome(type.isIncome)
        }
    }

    private fun inputCount(count: String) {
        _state.update {
            it.copy(count = count.toLongOrNull() ?: 0L)
        }
    }

    private fun inputDesc(desc: String) {
        _state.update {
            it.copy(desc = desc)
        }
    }

    private fun changeIncome(isIncome: Boolean) {
        _state.update {
            it.copy(isIncome = isIncome)
        }
    }

    private fun save(warning: () -> Unit, navigate: () -> Unit) {
        if (_state.value.desc == "") {
            warning()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.value.apply {
                operationRepo.insert(
                    Operation(
                        id = operationRepo.nextId(),
                        count = count,
                        desc = desc,
                        isIncome = isIncome,
                        accountId = accountId,
                        accountName = accountName,
                    )
                )

                val diff = if (isIncome) count else -count
                accountRepo.byId(accountId).collect { account ->
                    accountRepo.insert(account.copy(count = account.count + diff))
                }
            }
        }

        navigate()
    }
}