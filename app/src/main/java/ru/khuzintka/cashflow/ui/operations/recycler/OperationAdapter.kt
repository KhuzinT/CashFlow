package ru.khuzintka.cashflow.ui.operations.recycler

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.khuzintka.cashflow.R
import ru.khuzintka.cashflow.data.local.model.Operation
import ru.khuzintka.cashflow.databinding.OperationItemBinding


class OperationAdapter : RecyclerView.Adapter<OperationAdapter.OperationViewHolder>() {

    var data: List<Operation> = emptyList()
        set(value) {
            val diffUtil = OperationDiffUtil(field, value)
            val result = DiffUtil.calculateDiff(diffUtil)
            field = value
            result.dispatchUpdatesTo(this@OperationAdapter)
        }

    override fun getItemCount(): Int = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OperationItemBinding.inflate(inflater, parent, false)

        return OperationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OperationViewHolder, position: Int) {
        val operation = data[position]

        holder.binding.apply {
            trend.setImageResource(if (operation.isIncome) R.drawable.trending_up else R.drawable.trending_down)

            count.text = operation.count.toString()
            prefix.text = if (operation.isIncome) "+" else "-"

            val textColor = if (operation.isIncome) Color.GREEN else Color.RED
            count.setTextColor(textColor)
            prefix.setTextColor(textColor)
            suffix.setTextColor(textColor)

            desc.text = operation.desc

            account.text = operation.accountName
        }
    }

    class OperationViewHolder(val binding: OperationItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}