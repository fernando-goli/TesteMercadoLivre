package com.example.testemercadolivre.search.presentation.detailItem

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.search.domain.models.Product
import com.example.testemercadolivre.search.domain.usecase.GetProductUseCase
import com.example.testemercadolivre.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetailItemViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var getProductUseCase: GetProductUseCase
    private lateinit var viewModel: DetailItemViewModel

    private val product = Product(
        id = "1",
        title = "iphone",
        price = 2.0,
        initialQuantity = 7,
        soldQuantity = 2,
        condition = "new",
        thumbnail = "imagem",
        quantity = 1
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = DetailItemViewModel(getProductUseCase)
    }

    @Test
    fun `getProduct should be successful`() = runTest {
        coEvery { getProductUseCase("iphone") } returns Result.success(product)

        viewModel.getProduct("iphone")

        assertThat(viewModel.state.productResult.value).isEqualTo(product)
    }

    @Test
    fun `getProduct should be an error`() = runTest {
        val message = "unauthorized"
            coEvery { getProductUseCase("iphone") } returns Result.error(
                code = 401,
                message = "unauthorized"
            )

        viewModel.getProduct("iphone")

        assertThat(viewModel.state.error.value).isEqualTo(message)
    }

}