package com.example.farmacia_medicitas.data.repository

import com.example.farmacia_medicitas.core.Result
import com.example.farmacia_medicitas.data.mappers.toDomain
import com.example.farmacia_medicitas.data.model.Product
import com.example.farmacia_medicitas.data.remote.ApiService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteProductRepository @Inject constructor(
    private val api: ApiService
) : ProductRepository {
    override fun observeCache(): Flow<List<Product>> = flowOf(emptyList())

    override suspend fun getProducts(page: Int, pageSize: Int): Result<ProductPage> {
        return try {
            val response = api.getProducts(page = page, pageSize = pageSize)
            val products = response.results.map { it.toDomain() }
            Result.Success(
                ProductPage(
                    products = products,
                    count = response.count,
                    totalPages = response.total_pages,
                    currentPage = response.current_page,
                    pageSize = response.page_size,
                    hasNext = response.next != null,
                    hasPrevious = response.previous != null
                )
            )
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Error cargando productos", e)
        }
    }

    override suspend fun getProductById(id: String): Result<Product> {
        val intId = id.toIntOrNull() ?: return Result.Error("ID de producto inválido")
        return try {
            val product = api.getProduct(intId).toDomain()
            Result.Success(product)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Error obteniendo producto", e)
        }
    }
}
