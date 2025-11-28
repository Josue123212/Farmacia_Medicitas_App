package com.example.farmacia_medicitas.data.remote.dto.auth

// Envoltorio que coincide con el JSON del backend { message: string, data: { access, refresh, user } }
data class LoginEnvelope(
    val message: String,
    val data: LoginResponse
)