package com.example.testemercadolivre.data.repository

import androidx.paging.map
import com.example.testemercadolivre.CoroutineRule
import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.search.data.datasource.remote.api.SearchService
import com.example.testemercadolivre.search.data.models.ProductDTO
import com.example.testemercadolivre.search.data.models.SearchResponse
import com.example.testemercadolivre.search.data.repositories.SearchRepositoryImpl
import com.example.testemercadolivre.search.domain.repositories.SearchRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class SearchRepositoryImplTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    @MockK
    private lateinit var service: SearchService

    private lateinit var repository: SearchRepository
    private val slot = slot<String>()

    private val searchResponse = SearchResponse(
        results = listOf(
            ProductDTO(
                id = "ML1231",
                title = "Celular",
                price = 50.0,
                initial_quantity = 1,
                sold_quantity = 9,
                condition = "new",
                thumbnail = ""
            )
        )
    )

    private val productResponse =
        ProductDTO(
            id = "ML1231",
            title = "Celular",
            price = 50.0,
            initial_quantity = 1,
            sold_quantity = 9,
            condition = "new",
            thumbnail = ""
        )


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        repository = SearchRepositoryImpl(service)
    }

    @Test
    fun `getSearchProducts should return a success`() = runTest {
        val response = mockk<Response<SearchResponse>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns searchResponse
        coEvery { service.getSearchProducts(capture(slot)) } answers { response }

        val result = repository.getSearchProducts("iphone")

        assertThat(Result.success(result).data).isEqualTo(searchResponse)
    }

    @Test
    fun `getProduct should return a success`() = runTest {
        val response = mockk<Response<ProductDTO>>()
        every { response.isSuccessful } returns true
        every { response.body() } returns productResponse

        coEvery { service.getProduct(capture(slot)) } returns response

        val result = repository.getProduct("iphone")

        assertThat(result).isEqualTo(productResponse)
    }

//    @Test
//    fun `getProduct should return a error`() = runTest {
//        val response = mockk<Response<ProductDTO>>()
//        every { response.isSuccessful } returns true
//        every { response.body() } returns null
//        every { response.code() } returns 404
//        every { response.message() } returns "error"
//
//        coEvery { service.getProduct(capture(slot)) } returns response
//
//        val result = repository.getProduct("iphone")
//
//        assertThat((result as Result.Status.ERROR).).isEqualTo(404)
//    }
}