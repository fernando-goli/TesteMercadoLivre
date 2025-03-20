package com.example.testemercadolivre.search.domain.usecase

import com.example.testemercadolivre.search.domain.repositories.SearchRepository
import javax.inject.Inject

class GetProductUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String, accessToken: String) =
        searchRepository.getProduct(query, accessToken)
}