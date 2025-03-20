package com.example.testemercadolivre.search.presentation.searchTerm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testemercadolivre.core.common.fold
import com.example.testemercadolivre.search.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _code = MutableLiveData<String>()
    val code: LiveData<String> = _code

    val error: MutableLiveData<String> = MutableLiveData()

    fun getAccessToken(code: String) = viewModelScope.launch {
        authRepository.getAccessToken(code).fold(
            onSuccess = {
                getRefreshToken(it)
            },
            onError = { _, message ->
                error.postValue(message ?: "Ocorreu um erro desconhecido.")
            },
            onException = {
                error.postValue(it.message)
            }
        )
    }

    private fun getRefreshToken(refreshToken: String) = viewModelScope.launch {
        authRepository.getRefreshToken(refreshToken).fold(
            onSuccess = {
                _code.value = it
            },
            onError = { _, message ->
                error.postValue(message ?: "Ocorreu um erro desconhecido.")
            },
            onException = {
                error.postValue(it.message)
            }
        )
    }
}