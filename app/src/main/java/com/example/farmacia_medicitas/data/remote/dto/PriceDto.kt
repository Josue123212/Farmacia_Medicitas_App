package com.example.farmacia_medicitas.data.remote.dto

data class PriceDto(
    val currency: String,
    val amount: Double,
    val sale_amount: Double?
)