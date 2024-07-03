package ru.khuzintka.cashflow.util

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.LocaleListCompat
import java.util.*

///////////////////////////////////////////////////////////////////////////////////////////////////

const val onboardingSharPrefTag = "isFirstRun"

fun checkOnboarding(sharPref: SharedPreferencesManager, navigate: () -> Unit) {
    if (sharPref.getBoolean(onboardingSharPrefTag, true)) {
        sharPref.saveBoolean(onboardingSharPrefTag, false)
    } else {
        navigate()
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////

const val localeSharPrefTag = "locale"

fun setLocale(code: String) {
    when (code) {
        "ru" -> {
            val ru = Locale("ru", "RU")
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(ru))
        }
        "en" -> {
            val en = Locale("en", "EN")
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(en))
        }
        else -> {}
    }
}

fun checkLocale(sharPref: SharedPreferencesManager) {
    val code = sharPref.getString(localeSharPrefTag, "en")
    setLocale(code)
}

///////////////////////////////////////////////////////////////////////////////////////////////////

const val themeSharPrefTag = "isDarkMode"

fun setDarkTheme(isDarkMode: Boolean) {
    if (isDarkMode) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

fun checkDarkTheme(sharPref: SharedPreferencesManager) {
    val isDarkMode = sharPref.getBoolean(themeSharPrefTag, true)
    setDarkTheme(isDarkMode)
}

///////////////////////////////////////////////////////////////////////////////////////////////////

const val notificationsSharPrefTag = "isNotificationOn"

fun checkNotification(sharPref: SharedPreferencesManager, context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val areEnabled = notificationManager.areNotificationsEnabled()
        if (!areEnabled) {
            sharPref.saveBoolean(notificationsSharPrefTag, false)
        }
    } else {
        val areEnabled = NotificationManagerCompat.from(context).areNotificationsEnabled()
        if (!areEnabled) {
            sharPref.saveBoolean(notificationsSharPrefTag, false)
        }
    }
}