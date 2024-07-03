package ru.khuzintka.cashflow.ui.accounts.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.khuzintka.cashflow.data.local.model.Account
import ru.khuzintka.cashflow.databinding.AccountItemBinding

class AccountAdapter(private val accountActionListener: AccountActionListener) :
    RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    var data: List<Account> = emptyList()
        set(value) {
            val diffUtil = AccountDiffUtil(field, value)
            val result = DiffUtil.calculateDiff(diffUtil)
            field = value
            result.dispatchUpdatesTo(this@AccountAdapter)
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AccountItemBinding.inflate(inflater, parent, false)

        return AccountViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val account = data[position]

        holder.binding.apply {
            count.text = account.count.toString()
            name.text = account.name

            card.setOnClickListener {
                accountActionListener.onEdit(account)
            }
            newOperation.setOnClickListener {
                accountActionListener.onNewOperation(account)
            }
        }
    }

    class AccountViewHolder(val binding: AccountItemBinding) : RecyclerView.ViewHolder(binding.root)
}