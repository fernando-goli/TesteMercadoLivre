package com.example.testemercadolivre.search.presentation.listProducts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.testemercadolivre.search.domain.models.Product
import com.example.testemercadolivre.search.domain.usecase.GetSearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchProductsViewModel @Inject constructor(
    private val getSearchProductsUseCase: GetSearchProductsUseCase,
) : ViewModel() {

    private val _product = MutableStateFlow<Flow<PagingData<Product>>>(flowOf(PagingData.empty()))
    val product: StateFlow<Flow<PagingData<Product>>> = _product.asStateFlow()

    fun getListProducts(term: String, accessToken: String) = viewModelScope.launch {
        _product.value = getSearchProductsUseCase(term, accessToken).cachedIn(viewModelScope)
    }
}

