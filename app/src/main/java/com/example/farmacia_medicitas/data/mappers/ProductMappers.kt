package com.example.farmacia_medicitas.data.mappers

import com.example.farmacia_medicitas.core.ensureAbsoluteUrl
import com.example.farmacia_medicitas.data.model.Product
import com.example.farmacia_medicitas.data.remote.dto.ProductDto

fun ProductDto.toDomain(): Product {
    val priceValue = this.price?.let { it.sale_amount ?: it.amount } ?: 0.0
    val imagePath = this.images?.firstOrNull()?.image
    return Product(
        id = this.id.toString(),
        name = this.name,
        description = this.description ?: "",
        price = priceValue,
        imageUrl = imagePath?.let { ensureAbsoluteUrl(it) },
        inStock = true
    )
}

