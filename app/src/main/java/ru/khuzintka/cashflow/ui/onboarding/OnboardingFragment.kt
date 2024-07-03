package ru.khuzintka.cashflow.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.khuzintka.cashflow.R
import ru.khuzintka.cashflow.databinding.OnboardingFragmentBinding

class OnboardingFragment : Fragment() {
    private lateinit var binding: OnboardingFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = OnboardingFragmentBinding.inflate(layoutInflater, container, false)
        binding.start.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding_to_home)
        }

        return binding.root
    }

}