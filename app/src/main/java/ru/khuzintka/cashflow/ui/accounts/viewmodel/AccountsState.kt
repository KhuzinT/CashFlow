package ru.khuzintka.cashflow.ui.accounts.viewmodel

import ru.khuzintka.cashflow.data.local.model.Account

data class AccountsState(
    val items: MutableList<Account>,
    val total: Long,
    val isVisible: Boolean,
)