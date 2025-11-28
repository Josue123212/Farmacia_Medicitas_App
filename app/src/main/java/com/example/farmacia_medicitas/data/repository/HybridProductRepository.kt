package com.example.farmacia_medicitas.data.repository

import com.example.farmacia_medicitas.core.Result
import com.example.farmacia_medicitas.data.local.dao.ProductDao
import com.example.farmacia_medicitas.data.local.entity.ProductEntity
import com.example.farmacia_medicitas.data.model.Product
import com.example.farmacia_medicitas.data.mappers.toDomain
import com.example.farmacia_medicitas.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HybridProductRepository @Inject constructor(
    private val api: ApiService,
    private val dao: ProductDao
) : ProductRepository {

    override fun observeCache(): kotlinx.coroutines.flow.Flow<List<Product>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getProducts(page: Int, pageSize: Int): Result<ProductPage> = withContext(Dispatchers.IO) {
        try {
            val response = api.getProducts(page = page, pageSize = pageSize)
            val products = response.results.map { it.toDomain() }
            dao.replaceAll(products.map { it.toEntity() })
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
            val total = dao.getAll().size
            val totalPages = if (total == 0) 1 else ((total + pageSize - 1) / pageSize)
            val current = page.coerceIn(1, totalPages)
            val offset = ((current - 1).coerceAtLeast(0)) * pageSize
            val slice = dao.getPaged(pageSize, offset).map { it.toDomain() }
            Result.Success(
                ProductPage(
                    products = slice,
                    count = total,
                    totalPages = totalPages,
                    currentPage = current,
                    pageSize = pageSize,
                    hasNext = current < totalPages,
                    hasPrevious = current > 1
                )
            )
        }
    }

    override suspend fun getProductById(id: String): Result<Product> = withContext(Dispatchers.IO) {
        val intId = id.toIntOrNull()
        try {
            if (intId != null) {
                val product = api.getProduct(intId).toDomain()
                dao.upsertAll(listOf(product.toEntity()))
                Result.Success(product)
            } else {
                val cached = dao.getById(id)?.toDomain()
                if (cached != null) Result.Success(cached) else Result.Error("Producto no encontrado")
            }
        } catch (e: Exception) {
            val cached = dao.getById(id)?.toDomain()
            if (cached != null) Result.Success(cached) else Result.Error(e.localizedMessage ?: "Error obteniendo producto", e)
        }
    }
}

private fun Product.toEntity(): ProductEntity =
    ProductEntity(id = id, name = name, description = description, price = price, imageUrl = imageUrl, inStock = inStock)

private fun ProductEntity.toDomain(): Product =
    Product(id = id, name = name, description = description, price = price, imageUrl = imageUrl, inStock = inStock)
