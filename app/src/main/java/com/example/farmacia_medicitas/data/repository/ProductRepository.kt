package com.example.farmacia_medicitas.data.repository

import com.example.farmacia_medicitas.core.Result
import com.example.farmacia_medicitas.data.model.Product
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface ProductRepository {
    suspend fun getProducts(page: Int = 1, pageSize: Int = 20): Result<ProductPage>
    suspend fun getProductById(id: String): Result<Product>
    fun observeCache(): Flow<List<Product>>
}

data class ProductPage(
    val products: List<Product>,
    val count: Int,
    val totalPages: Int,
    val currentPage: Int,
    val pageSize: Int,
    val hasNext: Boolean,
    val hasPrevious: Boolean
)

class FakeProductRepository @Inject constructor() : ProductRepository {
    private val products = listOf(
        Product(
            id = "P-001",
            name = "Paracetamol 500mg",
            description = "Analgésico y antipirético",
            price = 12.50,
            imageUrl = null
        ),
        Product(
            id = "P-002",
            name = "Ibuprofeno 400mg",
            description = "Antiinflamatorio no esteroideo",
            price = 18.90,
            imageUrl = null
        ),
        Product(
            id = "P-003",
            name = "Vitamina C 1g",
            description = "Suplemento vitamínico",
            price = 22.00,
            imageUrl = null
        )
    )

    override suspend fun getProducts(page: Int, pageSize: Int): Result<ProductPage> {
        val fromIndex = ((page - 1).coerceAtLeast(0)) * pageSize
        val toIndex = (fromIndex + pageSize).coerceAtMost(products.size)
        val slice = if (fromIndex < products.size) products.subList(fromIndex, toIndex) else emptyList()
        val totalPages = if (products.isEmpty()) 1 else ((products.size + pageSize - 1) / pageSize)
        return Result.Success(
            ProductPage(
                products = slice,
                count = products.size,
                totalPages = totalPages,
                currentPage = page.coerceIn(1, totalPages),
                pageSize = pageSize,
                hasNext = page < totalPages,
                hasPrevious = page > 1
            )
        )
    }

    override suspend fun getProductById(id: String): Result<Product> {
        val product = products.find { it.id == id }
        return if (product != null) Result.Success(product) else Result.Error("Producto no encontrado")
    }

    override fun observeCache(): Flow<List<Product>> = flowOf(products)
}
