package com.example.testemercadolivre.search.data.datasource.remote.api

import com.example.testemercadolivre.core.common.Repository
import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.search.data.models.SearchResponse
import com.example.testemercadolivre.search.data.models.mapper.toDomain
import com.example.testemercadolivre.search.domain.models.Product
import javax.inject.Inject

interface IRemoteDataSource {
    suspend fun getSearchProducts(term: String, offset: Int): Result<SearchResponse?>
    suspend fun getProduct(product: String): Result<Product>
}

class RemoteDataSource @Inject constructor(private val searchService: SearchService) :
    Repository(), IRemoteDataSource {
    override suspend fun getSearchProducts(term: String, offset: Int): Result<SearchResponse?> {
        return http { searchService.getSearchProducts(term, offset = offset) }
    }

    override suspend fun getProduct(product: String): Result<Product> {
        try {
            val response = http { searchService.getProduct(product) }
            return when (response.status) {
                Result.Status.SUCCESS -> Result.success(response.data?.toDomain())
                Result.Status.ERROR -> Result.error(
                    message = response.message,
                    code = response.code
                )

                Result.Status.LOADING -> Result.loading()
            }
        } catch (e: Exception) {
            return Result.error(e.message)
        }
    }

}