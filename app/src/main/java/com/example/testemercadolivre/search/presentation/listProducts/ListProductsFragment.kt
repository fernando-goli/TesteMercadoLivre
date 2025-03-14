package com.example.testemercadolivre.search.presentation.listProducts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testemercadolivre.R
import com.example.testemercadolivre.databinding.FragmentListProductsBinding
import com.example.testemercadolivre.search.domain.models.Product
import com.example.testemercadolivre.search.domain.paging.LoaderAdapter
import com.example.testemercadolivre.search.presentation.listProducts.SearchResultsAdapter.AdapterListener
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListProductsFragment : Fragment() {

    private lateinit var binding: FragmentListProductsBinding
    private val viewModel: SearchProductsViewModel by viewModels<SearchProductsViewModel>()
    lateinit var product: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListProductsBinding.inflate(layoutInflater)
        arguments?.let {
            product = ListProductsFragmentArgs.fromBundle(it).product
            viewModel.getListProducts(product)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.searchInput.setQuery(product, false)
        binding.searchInput.setOnSearchClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        val adapter = SearchResultsAdapter(object : AdapterListener {

            override fun onClick(product: Product) {
                findNavController().navigate(
                    ListProductsFragmentDirections.actionListProductsFragmentToDetailItemFragment(
                        product.id
                    )
                )
            }
        })

        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        binding.listProducts.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            this.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoaderAdapter(),
                footer = LoaderAdapter()
            )
        }
        adapter.addLoadStateListener { loadState ->
            when (loadState.source.refresh) {
                LoadState.Loading -> {
                    binding.listProducts.isVisible = false
                    binding.progressBarExplore.isVisible = true
                }

                is LoadState.NotLoading -> {
                    if (loadState.append.endOfPaginationReached && adapter.itemCount < 1)
                        showError(getString(R.string.empty_list_products))
                    else
                        binding.progressBarExplore.isVisible = false
                    binding.listProducts.isVisible = true
                }

                is LoadState.Error -> showError(getString(R.string.error_loading_data))
            }
        }
        lifecycleScope.launch {
            viewModel.product.value.collect { items ->
                adapter.submitData(viewLifecycleOwner.lifecycle, items)
            }
        }
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
