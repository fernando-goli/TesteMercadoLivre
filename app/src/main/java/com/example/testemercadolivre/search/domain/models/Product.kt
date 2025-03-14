package com.example.testemercadolivre.search.domain.models

data class Product(
    val id: String,
    val title: String,
    val price: Double,
    val initialQuantity: Int,
    val soldQuantity: Int,
    val condition: String,
    val thumbnail: String,
    var quantity: Int = 1
)
