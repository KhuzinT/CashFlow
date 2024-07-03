package ru.khuzintka.cashflow.ui.accounts.recycler

import ru.khuzintka.cashflow.data.local.model.Account

interface AccountActionListener {
    fun onEdit(account: Account)
    fun onNewOperation(account: Account)
}