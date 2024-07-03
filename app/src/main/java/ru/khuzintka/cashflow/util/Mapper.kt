package ru.khuzintka.cashflow.util

import androidx.fragment.app.Fragment
import ru.khuzintka.cashflow.R
import ru.khuzintka.cashflow.ui.accounts.AccountsFragment
import ru.khuzintka.cashflow.ui.home.viewmodel.HomeFragmentTag
import ru.khuzintka.cashflow.ui.operations.OperationsFragment
import ru.khuzintka.cashflow.ui.operations.viewmodel.OperationFilter
import ru.khuzintka.cashflow.ui.settings.SettingsFragment

//////////////////////////////////////////////////////////////////////////////////////////////////
// Home Fragment

fun Fragment.toTag(): String {
    return when (this) {
        is AccountsFragment -> HomeFragmentTag.ACCOUNTS.tag
        is OperationsFragment -> HomeFragmentTag.OPERATIONS.tag
        is SettingsFragment -> HomeFragmentTag.SETTINGS.tag
        else -> null
    }!!
}

fun String.toSelectedItemId(): Int {
    return when (this) {
        HomeFragmentTag.ACCOUNTS.tag -> R.id.accounts
        HomeFragmentTag.OPERATIONS.tag -> R.id.operations
        HomeFragmentTag.SETTINGS.tag -> R.id.settings
        else -> null
    }!!
}

fun String.toFragment(): Fragment {
    return when (this) {
        HomeFragmentTag.ACCOUNTS.tag -> AccountsFragment()
        HomeFragmentTag.OPERATIONS.tag -> OperationsFragment()
        HomeFragmentTag.SETTINGS.tag -> SettingsFragment()
        else -> null
    }!!
}

//////////////////////////////////////////////////////////////////////////////////////////////////
// Operations Fragment

fun String.toOperationFilter(): OperationFilter {
    return when (this) {
        "all" -> OperationFilter.ALL
        "income" -> OperationFilter.INCOME
        "expenses" -> OperationFilter.EXPENSES
        else -> null
    }!!
}