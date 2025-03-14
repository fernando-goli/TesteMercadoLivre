package com.example.testemercadolivre.search.domain.repositories

import androidx.paging.PagingData
import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.search.domain.models.Product
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun getSearchProducts(term: String): Flow<PagingData<Product>>
    suspend fun getProduct(product: String): Result<Product>
}