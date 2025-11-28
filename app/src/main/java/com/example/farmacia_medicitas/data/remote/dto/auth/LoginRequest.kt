package com.example.farmacia_medicitas.data.remote.dto.auth

data class LoginRequest(
    val email_or_username: String,
    val password: String
)