package com.example.testemercadolivre.search.data.models

data class SearchResponse(
    val results: List<ProductDTO>
)

data class ProductDTO(
    val id: String,
    val title: String,
    val price: Double,
    val initial_quantity: Int,
    val sold_quantity: Int,
    val condition: String,
    val thumbnail: String,
    val warranty: String?,
    val descriptions: List<String>?

)