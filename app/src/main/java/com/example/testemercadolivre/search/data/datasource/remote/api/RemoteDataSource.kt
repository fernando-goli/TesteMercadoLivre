package com.example.testemercadolivre.search.data.datasource.remote.api

import com.example.testemercadolivre.core.common.RemoteApiVerifier
import com.example.testemercadolivre.core.common.Repository
import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.core.common.fold
import com.example.testemercadolivre.search.data.models.SearchResponse
import com.example.testemercadolivre.search.data.models.mapper.toDomain
import com.example.testemercadolivre.search.domain.models.Product
import javax.inject.Inject

interface RemoteDataSource {
    suspend fun getSearchProducts(
        term: String,
        offset: Int,
        accessToken: String,
    ): Result<SearchResponse>

    suspend fun getProduct(product: String, accessToken: String): Result<Product>
}

class RemoteDataSourceImpl @Inject constructor(private val searchService: SearchService) :
    Repository(RemoteApiVerifier), RemoteDataSource {
    override suspend fun getSearchProducts(
        term: String,
        offset: Int,
        accessToken: String,
    ): Result<SearchResponse> {
        val result = http {
            searchService.getSearchProducts(
                query = term,
                offset = offset,
                token = "Bearer $accessToken"
            )
        }
        return result.fold(
            onSuccess = {
                Result.Success(it)
                        },
            onError = { code, message ->
                Result.Error(code, message)
                      },
            onException = {
                Result.Exception(it) }

        )
    }

    override suspend fun getProduct(product: String, accessToken: String): Result<Product> {
        val response = http { searchService.getProduct(product, token = "Bearer $accessToken") }
        return response.fold(
            onSuccess = { Result.Success(it.toDomain()) },
            onError = { code, message -> Result.Error(code, message) },
            onException = { Result.Exception(it) }
        )
    }
}