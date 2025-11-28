package com.example.farmacia_medicitas.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.farmacia_medicitas.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun observeAll(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CartItemEntity)

    @Query("UPDATE cart_items SET quantity = quantity + 1 WHERE productId = :productId")
    suspend fun increase(productId: String)

    @Query("UPDATE cart_items SET quantity = CASE WHEN quantity > 0 THEN quantity - 1 ELSE 0 END WHERE productId = :productId")
    suspend fun decrease(productId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clear()

    @Query("SELECT quantity FROM cart_items WHERE productId = :productId")
    suspend fun getQuantity(productId: String): Int?

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items")
    suspend fun getTotalUnits(): Int

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteByProduct(productId: String)

    data class CartWithProduct(
        val productId: String,
        val name: String,
        val description: String,
        val price: Double,
        val imageUrl: String?,
        val inStock: Boolean,
        val quantity: Int
    )

    @Query(
        "SELECT p.id AS productId, p.name, p.description, p.price, p.imageUrl, p.inStock, c.quantity " +
        "FROM products p INNER JOIN cart_items c ON c.productId = p.id"
    )
    fun observeWithProducts(): Flow<List<CartWithProduct>>
}
