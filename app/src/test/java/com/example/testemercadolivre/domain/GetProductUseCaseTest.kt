package com.example.testemercadolivre.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.testemercadolivre.core.common.Result
import com.example.testemercadolivre.search.domain.models.Product
import com.example.testemercadolivre.search.domain.repositories.SearchRepository
import com.example.testemercadolivre.search.domain.usecase.GetProductUseCase
import com.example.testemercadolivre.util.MainCoroutineRule
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetProductUseCaseTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private lateinit var getProductUseCase: GetProductUseCase
    private var repository: SearchRepository = mockk()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        getProductUseCase = GetProductUseCase(repository)
    }

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

    @Test
    fun `getProduct should be successful`() = runTest {
        coEvery { repository.getProduct("iphone", "token") } returns
                Result.Success(product)

        val result = getProductUseCase("iphone", "token")

        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).data).isEqualTo(product)
    }


    @Test
    fun `getProduct should be error`() = runTest {
        coEvery { repository.getProduct("iphone", "token") } returns
                Result.Error(401, "unauthorized")

        val result = getProductUseCase("iphone", "token")

        assertThat(result).isInstanceOf(Result.Error::class.java)
        assertThat((result as Result.Error).code).isEqualTo(401)
        assertThat((result).message).isEqualTo("unauthorized")
    }


    @Test
    fun `getProduct should be exception`() = runTest {
        coEvery { repository.getProduct("iphone", "token") } returns
                Result.Exception(Exception("unauthorized"))

        val result = getProductUseCase("iphone", "token")

        assertThat(result).isInstanceOf(Result.Exception::class.java)
        assertThat((result as Result.Exception).exception.message).isEqualTo("unauthorized")
    }
}