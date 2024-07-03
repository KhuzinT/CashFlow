package ru.khuzintka.cashflow.ui.operations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.khuzintka.cashflow.R
import ru.khuzintka.cashflow.databinding.OperationsFragmentBinding
import ru.khuzintka.cashflow.ui.operations.recycler.OperationAdapter
import ru.khuzintka.cashflow.ui.operations.viewmodel.OperationFilter
import ru.khuzintka.cashflow.ui.operations.viewmodel.OperationsEvent
import ru.khuzintka.cashflow.ui.operations.viewmodel.OperationsViewModel

@AndroidEntryPoint
class OperationsFragment : Fragment() {
    private val viewModel by viewModels<OperationsViewModel>()

    private lateinit var adapter: OperationAdapter
    private lateinit var binding: OperationsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = OperationsFragmentBinding.inflate(inflater, container, false)
        adapter = OperationAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.operationsList.layoutManager = LinearLayoutManager(requireContext())
        binding.operationsList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val items = state.items.reversed()
                    adapter.data = when(state.filter) {
                        OperationFilter.ALL -> items
                        OperationFilter.INCOME -> items.filter { it.isIncome }
                        OperationFilter.EXPENSES -> items.filter { !it.isIncome }
                    }

                    binding.apply {
                        incomeCount.text = state.income.toString()
                        expensesCount.text = state.expenses.toString()

                        val incomeColor =
                            ContextCompat.getColor(
                                context!!,
                                if (state.filter == OperationFilter.INCOME) R.color.surface_variant else R.color.surface
                            )
                        incomeCard.setCardBackgroundColor(incomeColor)

                        val expensesColor =
                            ContextCompat.getColor(
                                context!!,
                                if (state.filter == OperationFilter.EXPENSES) R.color.surface_variant else R.color.surface
                            )
                        expensesCard.setCardBackgroundColor(expensesColor)
                    }

                    viewModel.event(OperationsEvent.Refresh)
                }
            }
        }

        binding.apply {
            incomeCard.setOnClickListener {
                viewModel.event(OperationsEvent.OnIncomeClick)
            }
            expensesCard.setOnClickListener {
                viewModel.event(OperationsEvent.OnExpensesClick)
            }
        }
    }
}