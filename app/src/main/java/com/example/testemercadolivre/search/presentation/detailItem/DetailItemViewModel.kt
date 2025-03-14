package com.example.testemercadolivre.search.presentation.detailItem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testemercadolivre.core.common.Result
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

    fun getProduct(product: String) = viewModelScope.launch {
        state.loading.postValue(true)
        val result = getProductUseCase(product)
        when (result.status) {
            Result.Status.LOADING ->  state.loading.postValue(true)
            Result.Status.SUCCESS -> {
                state.loading.postValue(false)
                if (result.data != null) state.productResult.postValue(result.data)
            }
            Result.Status.ERROR -> {
                state.loading.postValue(false)
                state.error.postValue(result.message)
            }
        }
    }
}

data class State(
    val productResult: MutableLiveData<Product> = MutableLiveData(),
    val loading: MutableLiveData<Boolean> = MutableLiveData(),
    val error: MutableLiveData<String> = MutableLiveData()
)

