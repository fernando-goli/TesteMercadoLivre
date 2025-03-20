package com.example.testemercadolivre.search.presentation.searchTerm

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.testemercadolivre.R
import com.example.testemercadolivre.core.common.RemoteApiVerifier
import com.example.testemercadolivre.core.util.TOKEN_CODE
import com.example.testemercadolivre.core.util.URL_CODE
import com.example.testemercadolivre.core.util.setupQueryTextSubmit
import com.example.testemercadolivre.databinding.FragmentSearchProductBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.net.URL

@AndroidEntryPoint
class SearchProductFragment : Fragment() {

    private lateinit var binding: FragmentSearchProductBinding
    private lateinit var webview: WebView
    private lateinit var sharedPref: SharedPreferences
    private val viewModel: AuthViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = context?.getSharedPreferences(TOKEN_CODE, Context.MODE_PRIVATE)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentSearchProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productSubmit()
        verifyToken()

        viewModel.code.observe(viewLifecycleOwner) {
            sharedPref.edit { putString("accessToken", it)?.apply() }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            showError(it)
        }
    }

    private fun verifyToken() {
        val getAccessToken = sharedPref.getString("accessToken", "")

        if (getAccessToken != null) {
            lifecycleScope.launch {
                val result = RemoteApiVerifier.isApiAvailableToken(getAccessToken)
                if (!result)
                    getCode()
            }
        } else {
            getCode()
        }
    }

    private fun getCode() {
        binding.webview.isVisible = true
        webview = binding.webview
        webview.settings.javaScriptEnabled = true
        webview.loadUrl(URL_CODE)
        setWebViewClient(webview)
    }

    private fun productSubmit() {
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

    private fun setWebViewClient(webView: WebView?) {
        webView?.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url?.contains("code=TG") == true) {
                    val code = URL(url).query.split("=").last()
                    sharedPref.edit { putString("code", code)?.apply() }
                    viewModel.getAccessToken(code)
                    binding.progress.isVisible = false
                    binding.webview.isVisible = false
                }
            }
        }
    }
}