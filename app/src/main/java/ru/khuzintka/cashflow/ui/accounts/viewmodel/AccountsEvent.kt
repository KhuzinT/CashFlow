package ru.khuzintka.cashflow.ui.accounts.viewmodel

sealed class AccountsEvent {
    object ChangeVisibility: AccountsEvent()
    object Refresh: AccountsEvent()
}