package ru.khuzintka.cashflow.ui.settings.viewmodel

data class SettingsState(
    val isDarkMode: Boolean,
    val locale: String,
    val isNotify: Boolean,
    val dollarRate: Double,
    val euroRate: Double,
)