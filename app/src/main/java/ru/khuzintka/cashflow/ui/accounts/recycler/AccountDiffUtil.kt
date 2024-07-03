package ru.khuzintka.cashflow.ui.accounts.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.khuzintka.cashflow.data.local.model.Account

class AccountDiffUtil(
    private val oldList: List<Account>,
    private val newList: List<Account>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldAccount = oldList[oldItemPosition]
        val newAccount = newList[newItemPosition]
        return oldAccount.id == newAccount.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldAccount = oldList[oldItemPosition]
        val newAccount = newList[newItemPosition]
        return oldAccount == newAccount
    }
}