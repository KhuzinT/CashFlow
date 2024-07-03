package ru.khuzintka.cashflow.ui.edit.account.viewmodel

import androidx.lifecycle.SavedStateHandle
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
import ru.khuzintka.cashflow.data.local.repo.OperationRepo
import ru.khuzintka.cashflow.ui.accounts.accountIdArgsTag
import javax.inject.Inject

const val unknownAccountId = -1L

@HiltViewModel
class EditAccountViewModel
@Inject constructor(
    private val accountRepo: AccountRepo,
    private val operationRepo: OperationRepo,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _state = MutableStateFlow(
        EditAccountState(
            count = 0L,
            name = "",
            isInTotal = true,
        )
    )
    val state = _state.asStateFlow()

    private val accountId = savedStateHandle.getStateFlow(accountIdArgsTag, unknownAccountId).value

    init {
        if (accountId != unknownAccountId) {
            viewModelScope.launch(Dispatchers.IO) {
                accountRepo.byId(accountId).collect { account ->
                    _state.update {
                        it.copy(
                            count = account.count,
                            name = account.name,
                            isInTotal = account.isInTotal
                        )
                    }
                }
            }
        }
    }

    fun event(type: EditAccountEvent) {
        when (type) {
            is EditAccountEvent.InputCount -> inputCount(type.count)
            is EditAccountEvent.InputName -> inputName(type.name)
            is EditAccountEvent.OnDeleteClick -> delete()
            is EditAccountEvent.OnSaveClick -> save(type.warning, type.navigate)
            is EditAccountEvent.SetInTotal -> inTotal(type.inTotal)
        }
    }

    private fun inputCount(count: String) {
        _state.update {
            it.copy(count = count.toLongOrNull() ?: 0L)
        }
    }

    private fun inputName(name: String) {
        _state.update {
            it.copy(name = name)
        }
    }

    private fun inTotal(inTotal: Boolean) {
        _state.update {
            it.copy(isInTotal = inTotal)
        }
    }

    private fun delete() {
        if (accountId != unknownAccountId) {
            viewModelScope.launch(Dispatchers.IO) {
                accountRepo.deleteById(accountId)
                operationRepo.deleteByAccountId(accountId)
            }
        }
    }

    private fun save(warning: () -> Unit, navigate: () -> Unit) {
        if (accountId == unknownAccountId && _state.value.name == "") {
            warning()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            _state.value.apply {
                if (accountId != unknownAccountId) {
                    accountRepo.update(
                        Account(
                            id = accountId,
                            count = count,
                            name = name,
                            isInTotal = isInTotal,
                        )
                    )
                } else {
                    accountRepo.insert(
                        Account(
                            id = accountRepo.nextId(),
                            count = count,
                            name = name,
                            isInTotal = isInTotal,
                        )
                    )
                }
            }
        }
        navigate()
    }
}

