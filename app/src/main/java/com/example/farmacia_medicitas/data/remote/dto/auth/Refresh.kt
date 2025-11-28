package com.example.farmacia_medicitas.data.remote.dto.auth

data class RefreshRequest(
    val refresh: String
)

data class RefreshResponse(
    val access: String
)