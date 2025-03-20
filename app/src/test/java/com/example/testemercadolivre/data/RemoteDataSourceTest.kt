package com.example.testemercadolivre.data

import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.core.common.getErrorCode
import com.example.testemercadolivre.search.data.datasource.remote.api.RemoteDataSource
import com.example.testemercadolivre.search.data.datasource.remote.api.RemoteDataSourceImpl
import com.example.testemercadolivre.search.data.datasource.remote.api.SearchService
import com.example.testemercadolivre.search.data.models.ProductDTO
import com.example.testemercadolivre.search.data.models.SearchResponse
import com.example.testemercadolivre.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.net.UnknownHostException


@ExperimentalCoroutinesApi
class RemoteDataSourceTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    private lateinit var searchService: SearchService

    private lateinit var remoteDataSource: RemoteDataSource

    private val product = ProductDTO(
        "MLB123",
        "test",
        3.0,
        1,
        9,
        "new",
        "",
        "",
        listOf()
    )

    private val searchResponse = SearchResponse(
        results = listOf(product)
    )

    @Before
    fun setUp() {
        remoteDataSource = RemoteDataSourceImpl(searchService)
    }

    @Test
    fun `getSearchProducts should be a successful`() = runTest {
        val response = mockk<Response<SearchResponse>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns searchResponse

        coEvery {
            searchService.getSearchProducts(
                "iphone",
                token = "Bearer token"
            )
        } returns response

        val result = remoteDataSource.getSearchProducts("iphone", 0, "token")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data.results.first().id).isEqualTo("MLB123")

        verify { response.isSuccessful }
        verify { response.body() }
        coVerify { searchService.getSearchProducts("iphone", token = "Bearer token") }
    }

    @Test
    fun `getSearchProducts should be a Error`() = runTest {
        val response = mockk<Response<SearchResponse>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.code() } returns 404
        every { response.message() } returns "error"

        coEvery {
            searchService.getSearchProducts(
                query = "iphone",
                token = "Bearer token"
            )
        } returns response

        val result = remoteDataSource.getSearchProducts("iphone", 0, "token")

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo("error")
        assertThat(result.getErrorCode()).isEqualTo(404)
        coVerify { searchService.getSearchProducts(query = "iphone", token = "Bearer token") }
        verify { response.isSuccessful }
        verify { response.body() }
        verify { response.code() }
    }

    @Test
    fun `getSearchProducts should be a Exception`() = runTest {
        val exception = UnknownHostException("error")
        coEvery {
            searchService.getSearchProducts(
                "iphone",
                token = "Bearer token"
            )
        } throws exception

        val result = remoteDataSource.getSearchProducts("iphone", 0, "token")

        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat((result as Result.Exception).exception.message).isEqualTo("error")
    }

    @Test
    fun `getProduct should be a successful`() = runTest {
        val response = mockk<Response<ProductDTO>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns product

        coEvery {
            searchService.getProduct(
                "iphone",
                token = "Bearer token"
            )
        } returns response

        val result = remoteDataSource.getProduct("iphone", "token")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data.id).isEqualTo("MLB123")
        verify { response.isSuccessful }
        verify { response.body() }
        coVerify { searchService.getProduct("iphone", token = "Bearer token") }
    }

    @Test
    fun `getProduct should be a Error`() = runTest {
        val response = mockk<Response<ProductDTO>>()
        every { response.isSuccessful } returns false
        every { response.body() } returns null
        every { response.code() } returns 404
        every { response.message() } returns "error"

        coEvery {
            searchService.getProduct(
                "iphone",
                token = "Bearer token"
            )
        } returns response

        val result = remoteDataSource.getProduct("iphone", "token")

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).message).isEqualTo("error")
        assertThat(result.getErrorCode()).isEqualTo(404)
        coVerify { searchService.getProduct("iphone", token = "Bearer token") }
        verify { response.isSuccessful }
        verify { response.body() }
        verify { response.code() }
    }

    @Test
    fun `getProduct should be a Exception`() = runTest {
        val exception = UnknownHostException("error")
        coEvery { searchService.getProduct("iphone", token = "Bearer token") } throws exception

        val result = remoteDataSource.getProduct("iphone", "token")

        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat((result as Result.Exception).exception.message).isEqualTo("error")
    }
}