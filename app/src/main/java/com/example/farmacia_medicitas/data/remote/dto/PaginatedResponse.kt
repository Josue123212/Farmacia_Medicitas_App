package com.example.farmacia_medicitas.data.remote.dto

data class PaginatedResponse<T>(
    val count: Int,
    val total_pages: Int,
    val current_page: Int,
    val page_size: Int,
    val next: String?,
    val previous: String?,
    val results: List<T>
)