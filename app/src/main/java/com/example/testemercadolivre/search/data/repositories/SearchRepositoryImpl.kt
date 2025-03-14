package com.example.testemercadolivre.search.data.repositories

import com.example.testemercadolivre.search.domain.repositories.SearchRepository
import javax.inject.Inject
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.*
import com.example.testemercadolivre.core.common.Repository
import com.example.testemercadolivre.search.domain.models.Product
import kotlinx.coroutines.flow.Flow
import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.search.data.datasource.remote.api.RemoteDataSource

private const val NETWORK_PAGE_SIZE = 10

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : Repository(), SearchRepository {

    override suspend fun getSearchProducts(term: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchPagingSource(term, remoteDataSource)
            }
        ).flow
    }

    override suspend fun getProduct(product: String): Result<Product> {
        try {
            val response = remoteDataSource.getProduct(product)
            return when (response.status) {
                Result.Status.SUCCESS -> Result.success(response.data)
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