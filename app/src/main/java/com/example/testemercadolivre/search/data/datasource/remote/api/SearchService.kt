package com.example.testemercadolivre.search.data.datasource.remote.api

import com.example.testemercadolivre.search.data.models.ProductDTO
import com.example.testemercadolivre.search.data.models.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchService {

    @GET("/sites/MLB/search")
    suspend fun getSearchProducts(
        @Query("q") query: String,
        @Query("limit") limit: String = "10",
        @Query("offset") offset: Int = 0,
    ): (Response<SearchResponse>)

    @GET("/items/{id}")
    suspend fun getProduct(
        @Path("id") itemId: String,
    ): Response<ProductDTO>
}