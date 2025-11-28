package com.example.farmacia_medicitas.di

import com.example.farmacia_medicitas.data.repository.ProductRepository
import com.example.farmacia_medicitas.data.repository.HybridProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindProductRepository(impl: HybridProductRepository): ProductRepository
}
