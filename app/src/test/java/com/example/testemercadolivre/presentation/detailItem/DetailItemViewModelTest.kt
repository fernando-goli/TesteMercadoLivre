package com.example.testemercadolivre.presentation.detailItem

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.search.domain.models.Product
import com.example.testemercadolivre.search.domain.usecase.GetProductUseCase
import com.example.testemercadolivre.search.presentation.detailItem.DetailItemViewModel
import com.example.testemercadolivre.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailItemViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @MockK
    private var getProductUseCase: GetProductUseCase = mockk()
    private lateinit var viewModel: DetailItemViewModel

    private val product = Product(
        id = "1",
        title = "iphone",
        price = 2.0,
        initialQuantity = 7,
        soldQuantity = 2,
        condition = "new",
        thumbnail = "imagem",
        quantity = 1,
        "",
        listOf()
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        viewModel = DetailItemViewModel(getProductUseCase)
    }

    @Test
    fun `getProduct should be successful`() = runTest {
        coEvery { getProductUseCase.invoke("iphone", "token") } returns
                Result.Success(product)

        viewModel.getProduct("iphone", "token")

        assertThat(viewModel.state.productResult.value).isEqualTo(product)
    }

    @Test
    fun `getProduct should be an error`() = runTest {
        val message = "unauthorized"
        coEvery { getProductUseCase.invoke("iphone", "token") } returns Result.Error(
            code = 401,
            message = "unauthorized"
        )

        viewModel.getProduct("iphone", "token")

        assertThat(viewModel.state.error.value).isEqualTo(message)
    }

    @Test
    fun `getProduct should be an exception`() = runTest {
        this.launch {
            val message = "unauthorized"
            coEvery { getProductUseCase.invoke("iphone", accessToken = "token") } returns
                    Result.Exception(exception = Exception(message))

            viewModel.getProduct("iphone", accessToken = "token")

            assertThat(viewModel.state.error.value).isEqualTo(message)
        }

    }

}