package ru.khuzintka.cashflow.app

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.khuzintka.cashflow.util.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val sharPref: SharedPreferencesManager,
) : ViewModel() {

    fun checkConfig(context: Context, navigate: () -> Unit) {
        checkOnboarding(sharPref, navigate)
        checkLocale(sharPref)
        checkDarkTheme(sharPref)
        checkNotification(sharPref, context)
    }
}