package com.example.farmacia_medicitas.core

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String, val cause: Throwable? = null) : Result<Nothing>()

    inline fun <R> fold(onSuccess: (T) -> R, onError: (String, Throwable?) -> R): R = when (this) {
        is Success -> onSuccess(data)
        is Error -> onError(message, cause)
    }
}

