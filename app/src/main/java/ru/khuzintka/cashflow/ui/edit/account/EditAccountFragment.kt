package ru.khuzintka.cashflow.ui.edit.account

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
import ru.khuzintka.cashflow.databinding.EditAccountFragmentBinding
import ru.khuzintka.cashflow.ui.edit.account.viewmodel.EditAccountEvent
import ru.khuzintka.cashflow.ui.edit.account.viewmodel.EditAccountViewModel
import ru.khuzintka.cashflow.ui.edit.operation.editCountKey

const val editNameKey = "editName"

@AndroidEntryPoint
class EditAccountFragment : Fragment() {
    private val viewModel by viewModels<EditAccountViewModel>()

    private lateinit var binding: EditAccountFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = EditAccountFragmentBinding.inflate(layoutInflater, container, false)
        if (savedInstanceState != null) {
            binding.apply {
                nameEdit.setText(savedInstanceState.getString(editNameKey))
                countEdit.setText(savedInstanceState.getString(editCountKey))
            }
        }
        return binding.root
    }

    // ToDo: сбросить фокус для editText
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    binding.apply {
                        totalSwitcher.isChecked = state.isInTotal

                        countEdit.hint = state.count.toString()
                        if (state.name != "") {
                            nameEdit.hint = state.name
                        }
                    }
                }
            }
        }

        binding.apply {
            cancel.setOnClickListener {
                navigateToHome()
            }
            save.setOnClickListener {
                viewModel.event(EditAccountEvent.OnSaveClick(
                    warning = {
                        Toast.makeText(context, getString(R.string.warning), Toast.LENGTH_SHORT)
                            .show()
                    },
                    navigate = {
                        navigateToHome()
                    }
                ))
            }
            delete.setOnClickListener {
                viewModel.event(EditAccountEvent.OnDeleteClick)
                navigateToHome()
            }

            countEdit.addTextChangedListener {
                viewModel.event(EditAccountEvent.InputCount(it.toString()))
            }
            nameEdit.addTextChangedListener {
                viewModel.event(EditAccountEvent.InputName(it.toString()))
            }

            totalSwitcher.setOnCheckedChangeListener { _, isChecked ->
                viewModel.event(EditAccountEvent.SetInTotal(isChecked))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.state.value.apply {
            outState.putString(editNameKey, name)
            outState.putString(editCountKey, count.toString())
        }
    }

    private fun navigateToHome() {
        val navController = findNavController()
        navController.navigate(R.id.action_edit_account_to_home)
        navController.popBackStack()
    }
}