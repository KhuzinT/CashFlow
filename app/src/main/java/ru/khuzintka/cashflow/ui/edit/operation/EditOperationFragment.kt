package ru.khuzintka.cashflow.ui.edit.operation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.khuzintka.cashflow.R
import ru.khuzintka.cashflow.databinding.EditOperationFragmentBinding
import ru.khuzintka.cashflow.ui.edit.operation.viewmodel.EditOperationEvent
import ru.khuzintka.cashflow.ui.edit.operation.viewmodel.EditOperationViewModel

const val editDescKey = "editDesc"
const val editCountKey = "editCount"

@AndroidEntryPoint
class EditOperationFragment : Fragment() {
    private val viewModel by viewModels<EditOperationViewModel>()

    private lateinit var binding: EditOperationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = EditOperationFragmentBinding.inflate(layoutInflater, container, false)
        if (savedInstanceState != null) {
            binding.apply {
                descEdit.setText(savedInstanceState.getString(editDescKey))
                countEdit.setText(savedInstanceState.getString(editCountKey))
            }
        }
        return binding.root
    }

    // ToDo: сбросить фокус для editText
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { state ->
                    binding.apply {
                        incomeSwitcher.isChecked = state.isIncome
                        accountName.text = state.accountName
                    }
                }
            }
        }

        binding.apply {
            cancel.setOnClickListener {
                navigateToHome()
            }
            save.setOnClickListener {
                viewModel.event(EditOperationEvent.OnSaveClick(
                    warning = {
                        Toast.makeText(context, getString(R.string.warning), Toast.LENGTH_SHORT)
                            .show()
                    },
                    navigate = {
                        navigateToHome()
                    }
                ))
            }

            countEdit.addTextChangedListener {
                viewModel.event(EditOperationEvent.InputCount(it.toString()))
            }
            descEdit.addTextChangedListener {
                viewModel.event(EditOperationEvent.InputDesc(it.toString()))
            }

            incomeSwitcher.setOnCheckedChangeListener { _, isChecked ->
                viewModel.event(EditOperationEvent.SetIncome(isChecked))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.state.value.apply {
            outState.putString(editDescKey, desc)
            outState.putString(editCountKey, count.toString())
        }
    }

    private fun navigateToHome() {
        val navController = findNavController()
        navController.navigate(R.id.action_edit_operation_to_home)
        navController.popBackStack()
    }
}