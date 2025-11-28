package com.example.farmacia_medicitas.data.cart

import com.example.farmacia_medicitas.data.local.dao.CartDao
import com.example.farmacia_medicitas.data.local.entity.CartItemEntity
import com.example.farmacia_medicitas.data.model.CartItem
import com.example.farmacia_medicitas.data.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val dao: CartDao
) : CartRepository {

    override val items: Flow<List<CartItem>> = dao.observeWithProducts().map { list ->
        list.map { e ->
            CartItem(
                product = Product(
                    id = e.productId,
                    name = e.name,
                    description = e.description,
                    price = e.price,
                    imageUrl = e.imageUrl,
                    inStock = e.inStock
                ),
                quantity = e.quantity
            )
        }
    }

    override suspend fun add(product: Product) = withContext(Dispatchers.IO) {
        val currentQty = dao.getQuantity(product.id) ?: 0
        val newEntity = CartItemEntity(
            productId = product.id,
            quantity = currentQty + 1
        )
        dao.upsert(newEntity)
    }

    override suspend fun increase(productId: String) = withContext(Dispatchers.IO) {
        dao.increase(productId)
    }

    override suspend fun decrease(productId: String) = withContext(Dispatchers.IO) {
        dao.decrease(productId)
        val qty = dao.getQuantity(productId) ?: 0
        if (qty <= 0) {
            dao.deleteByProduct(productId)
        }
    }

    override suspend fun clear() = withContext(Dispatchers.IO) {
        dao.clear()
    }
}
