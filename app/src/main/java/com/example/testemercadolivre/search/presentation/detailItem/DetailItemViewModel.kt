package com.example.testemercadolivre.search.presentation.detailItem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testemercadolivre.core.common.fold
import com.example.testemercadolivre.core.common.isPageNotFound
import com.example.testemercadolivre.search.domain.models.Product
import com.example.testemercadolivre.search.domain.usecase.GetProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailItemViewModel @Inject constructor(
    private val getProductUseCase: GetProductUseCase,
) : ViewModel() {
    val state: State = State()

    fun getProduct(product: String, accessToken: String) = viewModelScope.launch {
        state.loading.postValue(true)
        val result = getProductUseCase(product, accessToken)
        result.fold(
            onSuccess = { state.productResult.postValue(it) },
            onError = { code, message ->
                if (isPageNotFound(code)) state.error.postValue("Produto não encontrado")
                else state.error.postValue(message)
            },
            onException = { state.error.postValue(it.message) }
        )
        state.loading.postValue(false)
    }
}

data class State(
    val productResult: MutableLiveData<Product> = MutableLiveData(),
    val loading: MutableLiveData<Boolean> = MutableLiveData(),
    val error: MutableLiveData<String> = MutableLiveData(),
)

