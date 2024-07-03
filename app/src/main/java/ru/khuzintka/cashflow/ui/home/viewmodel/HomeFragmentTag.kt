package ru.khuzintka.cashflow.ui.home.viewmodel

const val activeHomeFragmentSharPrefTag = "activeFragment"

enum class HomeFragmentTag(val tag: String) {
    ACCOUNTS("accounts"),
    OPERATIONS("operations"),
    SETTINGS("settings"),
}