package com.example.testemercadolivre.search.data.repositories

import com.example.testemercadolivre.search.domain.repositories.SearchRepository
import javax.inject.Inject
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.*
import com.example.testemercadolivre.core.common.RemoteApiVerifier
import com.example.testemercadolivre.core.common.Repository
import com.example.testemercadolivre.search.domain.models.Product
import kotlinx.coroutines.flow.Flow
import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.core.common.fold
import com.example.testemercadolivre.search.data.datasource.remote.api.RemoteDataSourceImpl

private const val NETWORK_PAGE_SIZE = 10

class SearchRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSourceImpl,
) : Repository(RemoteApiVerifier), SearchRepository {

    override suspend fun getSearchProducts(
        term: String,
        accessToken: String,
    ): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchPagingSource(term, remoteDataSource, accessToken)
            }
        ).flow
    }

    override suspend fun getProduct(product: String, accessToken: String): Result<Product> {
        val response = remoteDataSource.getProduct(product, accessToken)
        return response.fold(
            onSuccess = { Result.Success(it) },
            onError = { code, message -> Result.Error(code, message) },
            onException = { Result.Exception(it) }
        )
    }
}