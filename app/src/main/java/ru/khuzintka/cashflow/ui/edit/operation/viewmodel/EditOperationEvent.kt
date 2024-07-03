package ru.khuzintka.cashflow.ui.edit.operation.viewmodel


sealed class EditOperationEvent {
    data class InputCount(val count: String) : EditOperationEvent()
    data class InputDesc(val desc: String) : EditOperationEvent()
    data class OnSaveClick(val warning: () -> Unit, val navigate: () -> Unit) : EditOperationEvent()
    data class SetIncome(val isIncome: Boolean) : EditOperationEvent()
}