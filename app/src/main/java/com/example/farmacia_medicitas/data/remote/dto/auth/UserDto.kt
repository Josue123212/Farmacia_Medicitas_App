package com.example.farmacia_medicitas.data.remote.dto.auth

data class UserDto(
    val id: Int,
    val username: String?,
    val email: String?,
    val first_name: String?,
    val last_name: String?,
    val role: String?,
    val patient_profile_id: Int?
)