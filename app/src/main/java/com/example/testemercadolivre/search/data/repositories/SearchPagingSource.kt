package com.example.testemercadolivre.search.data.repositories

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.testemercadolivre.core.common.getException
import com.example.testemercadolivre.core.common.getSuccess
import com.example.testemercadolivre.search.data.datasource.remote.api.RemoteDataSource
import com.example.testemercadolivre.search.data.models.mapper.toDomain
import com.example.testemercadolivre.search.domain.models.Product
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

private const val OFFSET_INDEX = 0

class SearchPagingSource @Inject constructor(
    private val term: String,
    private val searchService: RemoteDataSource,
    private val accessToken: String,
) : PagingSource<Int, Product>() {

    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition = anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition = anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        val offset = params.key ?: OFFSET_INDEX
        return try {
            val response = searchService.getSearchProducts(term, offset = offset,  accessToken = accessToken)
            response.getException()?.let { throw it }
            val products = response.getSuccess()?.results?.map { it.toDomain() }.orEmpty()
            val prevKey = if (offset > OFFSET_INDEX) offset - 1 else null
            val nextKey = if (products.isNotEmpty()) offset + 1 else null
            LoadResult.Page(
                data = products,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (exception: UnknownHostException) {
            LoadResult.Error(exception)
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}

