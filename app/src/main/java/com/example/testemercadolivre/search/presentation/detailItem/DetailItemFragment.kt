package com.example.testemercadolivre.search.presentation.detailItem

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.testemercadolivre.R
import com.example.testemercadolivre.core.util.toCurrency
import com.example.testemercadolivre.databinding.FragmentDetailItemBinding
import com.example.testemercadolivre.search.domain.models.Product
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailItemFragment : Fragment() {

    private lateinit var binding: FragmentDetailItemBinding
    private lateinit var product: String
    private val viewModel: DetailItemViewModel by viewModels<DetailItemViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailItemBinding.inflate(inflater, container, false)
        arguments?.let {
            product = DetailItemFragmentArgs.fromBundle(it).product
            viewModel.getProduct(product)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backButton.setOnClickListener { findNavController().popBackStack() }
        observer()
        binding.searchInput.setQuery(product, false)
    }

    private fun observer() {
        viewModel.state.productResult.observe(viewLifecycleOwner) {
            bind(it)
        }

        viewModel.state.loading.observe(viewLifecycleOwner) {
            binding.loadingBar.isVisible = it == true
        }

        viewModel.state.error.observe(viewLifecycleOwner) {
            showError(it)
        }
    }

    private fun bind(product: Product) {
        binding.loadingBar.isVisible = false
        binding.titleProduct.text = product.title
        binding.condition.text = product.condition
        binding.priceProduct.text = product.price.toCurrency()
        binding.soldQuantity.text =
            getString(R.string.label_sold_quantity, product.soldQuantity.toString())
        binding.availableQuantity.text =
            getString(R.string.label_available_quantity, product.initialQuantity.toString())
        Glide.with(this)
            .load(product.thumbnail.replace("http:", "https:"))
            .into(binding.imageProduct)
    }

    private fun showError(
        message: String, duration: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(
            binding.root, message, duration
        ).setAction(
            R.string.search_error_dismiss
        ) {}.show()
    }
}