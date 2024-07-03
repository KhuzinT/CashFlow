package ru.khuzintka.cashflow.ui.accounts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.khuzintka.cashflow.R
import ru.khuzintka.cashflow.data.local.model.Account
import ru.khuzintka.cashflow.databinding.AccountsFragmentBinding
import ru.khuzintka.cashflow.ui.accounts.recycler.AccountActionListener
import ru.khuzintka.cashflow.ui.accounts.recycler.AccountAdapter
import ru.khuzintka.cashflow.ui.accounts.viewmodel.AccountsEvent
import ru.khuzintka.cashflow.ui.accounts.viewmodel.AccountsViewModel

const val accountIdArgsTag = "accountId"

@AndroidEntryPoint
class AccountsFragment : Fragment() {
    private val viewModel by viewModels<AccountsViewModel>()

    private lateinit var adapter: AccountAdapter
    private lateinit var binding: AccountsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = AccountsFragmentBinding.inflate(inflater, container, false)
        adapter = AccountAdapter(object : AccountActionListener {
            override fun onNewOperation(account: Account) {
                val bundle = bundleOf(accountIdArgsTag to account.id)
                findNavController().navigate(R.id.action_home_to_edit_operation, bundle)
            }

            override fun onEdit(account: Account) {
                val bundle = bundleOf(accountIdArgsTag to account.id)
                findNavController().navigate(R.id.action_home_to_edit_account, bundle)
            }
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.accountsList.layoutManager = LinearLayoutManager(requireContext())
        binding.accountsList.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state.collect { state ->
                    adapter.data = state.items

                    binding.apply {
                        if (state.isVisible) {
                            total.text = state.total.toString()
                            visibility.setImageResource(R.drawable.visibility_off)
                        } else {
                            total.text = getString(R.string.hide)
                            visibility.setImageResource(R.drawable.visibility)

                            // ToDo: не знаю, как скрыть баланс каждого item в recyclerView
                        }
                    }

                    viewModel.event(AccountsEvent.Refresh)
                }
            }
        }

        binding.apply {
            cashflow.setOnClickListener {
                Toast.makeText(context, getString(R.string.author), Toast.LENGTH_SHORT).show()
            }
            visibility.setOnClickListener {
                viewModel.event(AccountsEvent.ChangeVisibility)
            }
            newAccount.setOnClickListener {
                findNavController().navigate(R.id.action_home_to_edit_account)
            }
        }
    }
}