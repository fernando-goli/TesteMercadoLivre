package com.example.testemercadolivre.search.data.models.mapper

import com.example.testemercadolivre.search.data.models.ProductDTO
import com.example.testemercadolivre.search.domain.models.Product


fun ProductDTO.toDomain(): Product {
    return Product(
        id = id,
        title = title,
        price = price,
        initialQuantity = initial_quantity,
        soldQuantity = sold_quantity,
        condition = condition,
        thumbnail = thumbnail
    )
}
