package com.example.farmacia_medicitas.data.remote

import com.example.farmacia_medicitas.data.remote.dto.PaginatedResponse
import com.example.farmacia_medicitas.data.remote.dto.ProductDto
import com.example.farmacia_medicitas.data.remote.dto.auth.UserDto
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("ecommerce/products/")
    suspend fun getProducts(
        @Query("page") page: Int? = null,
        @Query("page_size") pageSize: Int? = null,
        @Query("search") search: String? = null,
        @Query("category") categoryId: Int? = null
    ): PaginatedResponse<ProductDto>

    @GET("ecommerce/products/{id}/")
    suspend fun getProduct(
        @Path("id") id: Int
    ): ProductDto

    // Auth endpoints
    @POST("users/auth/login/")
    suspend fun login(@Body body: com.example.farmacia_medicitas.data.remote.dto.auth.LoginRequest): com.example.farmacia_medicitas.data.remote.dto.auth.LoginEnvelope

    @POST("users/auth/refresh/")
    suspend fun refresh(@Body body: com.example.farmacia_medicitas.data.remote.dto.auth.RefreshRequest): com.example.farmacia_medicitas.data.remote.dto.auth.RefreshResponse

    // Protected endpoint: current authenticated user (según backend: /api/users/me/)
    @GET("users/me/")
    suspend fun me(): UserDto
}