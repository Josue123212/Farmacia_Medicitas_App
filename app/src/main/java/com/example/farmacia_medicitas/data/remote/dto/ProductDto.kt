package com.example.farmacia_medicitas.data.remote.dto

data class ProductDto(
    val id: Int,
    val name: String,
    val description: String?,
    val category: CategoryDto?,
    val images: List<ProductImageDto>?,
    val price: PriceDto?,
    val is_active: Boolean,
    val created_at: String
)