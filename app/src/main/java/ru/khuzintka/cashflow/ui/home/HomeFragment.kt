package ru.khuzintka.cashflow.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.khuzintka.cashflow.R
import ru.khuzintka.cashflow.databinding.HomeFragmentBinding
import ru.khuzintka.cashflow.ui.accounts.AccountsFragment
import ru.khuzintka.cashflow.ui.home.viewmodel.HomeViewModel
import ru.khuzintka.cashflow.ui.operations.OperationsFragment
import ru.khuzintka.cashflow.ui.settings.SettingsFragment
import ru.khuzintka.cashflow.util.toFragment

// ToDo: была таже проблема с сохранением текущего фрагмента, что обсуждали в чате
// решил тем, что храню тэг фрагмента, который нужно открыть, в sharPref

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val viewModel by viewModels<HomeViewModel>()

    private lateinit var binding: HomeFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadFragment(viewModel.getTag().toFragment())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = HomeFragmentBinding.inflate(layoutInflater, container, false)

        binding.apply {
            bottomNavigation.selectedItemId = viewModel.getId()

            bottomNavigation.setOnItemSelectedListener { menuItem ->
                if (menuItem.itemId == bottomNavigation.selectedItemId) {
                    return@setOnItemSelectedListener false
                }

                // ToDo: раньше хранил фрагменты как поля класса,
                // но были проблемы с обновлением состояния списков
                // сейчас каждый раз создаю заново. От этого при переходе на экран
                // немного дерганный UI, но это хотя бы работает
                when (menuItem.itemId) {
                    R.id.accounts -> {
                        loadFragment(AccountsFragment())
                    }
                    R.id.operations -> {
                        loadFragment(OperationsFragment())
                    }
                    R.id.settings -> {
                        loadFragment(SettingsFragment())
                    }
                    else -> false
                }
            }
        }

        return binding.root
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }
        viewModel.save(fragment)
        return true
    }
}
