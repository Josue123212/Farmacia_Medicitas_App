package com.example.farmacia_medicitas.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.farmacia_medicitas.data.local.dao.CartDao
import com.example.farmacia_medicitas.data.local.dao.ProductDao
import com.example.farmacia_medicitas.data.local.entity.CartItemEntity
import com.example.farmacia_medicitas.data.local.entity.ProductEntity

@Database(
    entities = [CartItemEntity::class, ProductEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun productDao(): ProductDao
}
