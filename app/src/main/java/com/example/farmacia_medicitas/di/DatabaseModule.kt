package com.example.farmacia_medicitas.di

import android.content.Context
import androidx.room.Room
import com.example.farmacia_medicitas.data.cart.CartRepository
import com.example.farmacia_medicitas.data.cart.CartRepositoryImpl
import com.example.farmacia_medicitas.data.local.dao.CartDao
import com.example.farmacia_medicitas.data.local.dao.ProductDao
import com.example.farmacia_medicitas.data.local.db.AppDatabase
import com.example.farmacia_medicitas.data.local.db.MIGRATION_1_2
import com.example.farmacia_medicitas.data.local.db.MIGRATION_2_3
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "medicitas.db")
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()

    @Provides
    fun provideCartDao(db: AppDatabase): CartDao = db.cartDao()

    @Provides
    fun provideProductDao(db: AppDatabase): ProductDao = db.productDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CartBindingsModule {
    @Binds
    @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository
}
