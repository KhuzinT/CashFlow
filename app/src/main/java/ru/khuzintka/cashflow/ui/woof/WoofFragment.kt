package ru.khuzintka.cashflow.ui.woof

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import ru.khuzintka.cashflow.R
import ru.khuzintka.cashflow.databinding.WoofFragmentBinding

@AndroidEntryPoint
class WoofFragment : Fragment() {
    private val viewModel by viewModels<WoofViewModel>()

    private lateinit var binding: WoofFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = WoofFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadImage()

        binding.apply {
            update.setOnClickListener {
                loadImage()
            }
        }
    }

    private fun loadImage() {
        viewModel.newImage()
        Glide
            .with(requireActivity())
            .load(viewModel.url.value)
            .placeholder(R.drawable.happy)
            .into(binding.woofImage)
    }

}