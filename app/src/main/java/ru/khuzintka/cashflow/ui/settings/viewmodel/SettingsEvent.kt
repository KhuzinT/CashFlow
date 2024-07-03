package ru.khuzintka.cashflow.ui.settings.viewmodel

import androidx.fragment.app.FragmentActivity

sealed class SettingsEvent {
    data class SetDarkMode(val isDarkMode: Boolean) : SettingsEvent()
    data class SetLocale(val locale: String) : SettingsEvent()
    data class SetNotify(val isNotify: Boolean) : SettingsEvent()
    object LoadRates : SettingsEvent()
}