package ru.khuzintka.cashflow.ui.settings.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.khuzintka.cashflow.data.remote.repo.CurrencyRepo
import ru.khuzintka.cashflow.util.*
import javax.inject.Inject

const val unknownRate = 0.0
const val settingsLog = "SettingsLog"

@HiltViewModel
class SettingsViewModel
@Inject constructor(
    private val repo: CurrencyRepo,
    private val sharPref: SharedPreferencesManager,
) : ViewModel() {

    private val _state = MutableStateFlow(
        SettingsState(
            isDarkMode = true,
            locale = "en",
            isNotify = false,
            dollarRate = unknownRate,
            euroRate = unknownRate,
        )
    )
    val state: StateFlow<SettingsState> = _state

    init {
        init()
    }

    fun event(type: SettingsEvent) {
        when (type) {
            is SettingsEvent.SetDarkMode -> darkMode(type.isDarkMode)
            is SettingsEvent.SetLocale -> locale(type.locale)
            is SettingsEvent.SetNotify -> notify(type.isNotify)
            is SettingsEvent.LoadRates -> loadRates()
        }
    }

    private fun init() {
        _state.update {
            it.copy(
                isDarkMode = sharPref.getBoolean(themeSharPrefTag, true),
                locale = sharPref.getString(localeSharPrefTag, "en"),
                isNotify = sharPref.getBoolean(notificationsSharPrefTag, false),
            )
        }
    }

    private fun darkMode(isDarkMode: Boolean) {
        sharPref.saveBoolean(themeSharPrefTag, isDarkMode)
        setDarkTheme(isDarkMode)

        _state.update {
            it.copy(isDarkMode = isDarkMode)
        }
    }

    private fun locale(locale: String) {
        sharPref.saveString(localeSharPrefTag, locale)
        setLocale(locale)

        _state.update {
            it.copy(locale = locale)
        }
    }

    private fun notify(isNotify: Boolean) {
        sharPref.saveBoolean(notificationsSharPrefTag, isNotify)
        _state.update { it.copy(isNotify = isNotify) }
    }

    private fun loadRates() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getDollarRate()
                .catch {
                    Log.e(settingsLog, it.message ?: "¯\\_(ツ)_/¯")
                }
                .collect { currency ->
                    _state.update { it.copy(dollarRate = currency.value) }
                    Log.d(settingsLog, "Dollar = ${currency.value}")
                }

            repo.getEuroRate()
                .catch {
                    Log.e(settingsLog, it.message ?: "¯\\_(ツ)_/¯")
                }
                .collect { currency ->
                    _state.update { it.copy(euroRate = currency.value) }
                    Log.d(settingsLog, "Euro = ${currency.value}")
                }
        }
    }
}