package ru.khuzintka.cashflow.ui.edit.account.viewmodel

sealed class EditAccountEvent {
    data class InputCount(val count: String) : EditAccountEvent()
    data class InputName(val name: String) : EditAccountEvent()
    object OnDeleteClick : EditAccountEvent()
    data class OnSaveClick(val warning: () -> Unit, val navigate: () -> Unit) : EditAccountEvent()
    data class SetInTotal(val inTotal: Boolean) : EditAccountEvent()
}