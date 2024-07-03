package ru.khuzintka.cashflow.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager constructor(context: Context) {
    private val tag = "sharedPreferences"
    private val sharPref: SharedPreferences

    init {
        sharPref = context.getSharedPreferences(tag, Context.MODE_PRIVATE)
    }

    fun saveString(key: String, value: String) {
        sharPref.edit().putString(key, value).apply()
    }

    fun getString(key: String, default: String): String {
        return sharPref.getString(key, default)!!
    }

    fun saveBoolean(key: String, value: Boolean) {
        sharPref.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, default: Boolean): Boolean {
        return sharPref.getBoolean(key, default)
    }
}