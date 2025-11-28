package com.example.farmacia_medicitas.data.cart

import com.example.farmacia_medicitas.data.model.CartItem
import com.example.farmacia_medicitas.data.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    val items: Flow<List<CartItem>>
    suspend fun add(product: Product)
    suspend fun increase(productId: String)
    suspend fun decrease(productId: String)
    suspend fun clear()
}

