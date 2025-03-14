package com.example.testemercadolivre.search.presentation.searchTerm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.testemercadolivre.R
import com.example.testemercadolivre.core.util.setupQueryTextSubmit
import com.example.testemercadolivre.databinding.FragmentSearchProductBinding
import com.google.android.material.snackbar.Snackbar

class SearchProductFragment : Fragment() {

    private lateinit var binding: FragmentSearchProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchInput.setupQueryTextSubmit { term ->
            if (term?.isNotEmpty() == true && term.isNotBlank()) {
                findNavController().navigate(
                    SearchProductFragmentDirections.actionSearchProductFragmentToListProductsFragment(
                        term
                    )
                )
            }
        }

    }

    private fun showError(message: String, duration: Int = Snackbar.LENGTH_INDEFINITE) {
        Snackbar.make(
            binding.root, message, duration
        ).setAction(
            R.string.search_error_dismiss
        ) {}.show()
    }
}