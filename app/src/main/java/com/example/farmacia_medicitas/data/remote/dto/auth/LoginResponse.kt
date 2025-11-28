package com.example.farmacia_medicitas.data.remote.dto.auth

data class LoginResponse(
    val access: String,
    val refresh: String,
    val user: UserDto
)