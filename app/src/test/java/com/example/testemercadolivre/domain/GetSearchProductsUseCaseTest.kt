package com.example.testemercadolivre.domain

import androidx.paging.PagingData
import com.example.testemercadolivre.search.domain.models.Product
import com.example.testemercadolivre.search.domain.repositories.SearchRepository
import com.example.testemercadolivre.search.domain.usecase.GetSearchProductsUseCase
import com.example.testemercadolivre.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetSearchProductsUseCaseTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var getSearchProductsUseCase: GetSearchProductsUseCase
    private val repository = mockk<SearchRepository>()
    private val slot = slot<String>()

    @Before
    fun setUp() {
        getSearchProductsUseCase = GetSearchProductsUseCase(repository)
    }

    @Test
    fun `getSearchProductsUseCase should be successful`() = runTest {
        val product = mockk<Flow<PagingData<Product>>>()
        coEvery {
            repository.getSearchProducts(
                capture(slot),
                "token"
            )
        } returns product

        val result = getSearchProductsUseCase.invoke("iphone", "token")

        assertThat(slot.captured).isEqualTo("iphone")
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(result)
    }


    @Test
    fun `getProduct should be empty`() = runTest {
        val product = flowOf(PagingData.empty<Product>())
        coEvery { repository.getSearchProducts("iphone", "token") } returns
                product

        val result = getSearchProductsUseCase("iphone", "token")

        assertThat(result).isEqualTo(product)
    }

}