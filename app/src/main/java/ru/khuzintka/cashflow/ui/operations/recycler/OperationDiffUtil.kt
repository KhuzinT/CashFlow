package ru.khuzintka.cashflow.ui.operations.recycler

import androidx.recyclerview.widget.DiffUtil
import ru.khuzintka.cashflow.data.local.model.Operation

class OperationDiffUtil(
    private val oldList: List<Operation>,
    private val newList: List<Operation>,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldOperation = oldList[oldItemPosition]
        val newOperation = newList[newItemPosition]
        return oldOperation.id == newOperation.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldOperation = oldList[oldItemPosition]
        val newOperation = newList[newItemPosition]
        return oldOperation == newOperation
    }
}