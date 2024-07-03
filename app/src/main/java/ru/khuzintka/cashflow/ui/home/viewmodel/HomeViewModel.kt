package ru.khuzintka.cashflow.ui.home.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.khuzintka.cashflow.util.SharedPreferencesManager
import ru.khuzintka.cashflow.util.toSelectedItemId
import ru.khuzintka.cashflow.util.toTag
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val sharPref: SharedPreferencesManager,
) : ViewModel() {

    fun save(fragment: Fragment) {
        sharPref.saveString(activeHomeFragmentSharPrefTag, fragment.toTag())
    }

    fun getTag(): String {
        return sharPref.getString(activeHomeFragmentSharPrefTag, HomeFragmentTag.ACCOUNTS.tag)
    }

    fun getId(): Int {
        return getTag().toSelectedItemId()
    }
}