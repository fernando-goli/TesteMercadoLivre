package com.example.testemercadolivre.presentation.listProduct

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.paging.map
import com.example.testemercadolivre.search.domain.models.Product
import com.example.testemercadolivre.search.domain.usecase.GetSearchProductsUseCase
import com.example.testemercadolivre.search.presentation.listProducts.SearchProductsViewModel
import com.example.testemercadolivre.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListProductsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    private val usecase: GetSearchProductsUseCase = mockk()
    private lateinit var viewModel: SearchProductsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = SearchProductsViewModel(usecase)
    }

    @Test
    fun `getProducts should be successful`() = runTest {
        val prod = Product("1", "iphone", 2.0, 7, 2, "new", "imagem", 1,"", listOf())
        val list = listOf(prod)
        val pagingList = PagingData.from(list)
        val product = flowOf(pagingList)
        coEvery { usecase.invoke("iphone", accessToken = "token") } returns product
        this.launch {
            viewModel.getListProducts("iphone", accessToken = "token")
        }
        viewModel.product.value.single().map { assertThat(it.id).isEqualTo("1")}
    }

    @Test
    fun `getProducts should be empty`() = runTest {
        val product = flowOf(PagingData.empty<Product>())
        assertThat(viewModel.product.value.single()).isEqualTo(product.single())
        coEvery { usecase.invoke("iphone", accessToken = "token") } returns product
        this.launch {
            viewModel.getListProducts("iphone", accessToken = "token")
        }
        assertThat(viewModel.product.value.single()).isEqualTo(product.single())
    }
}