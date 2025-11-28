package com.example.farmacia_medicitas.core

import com.example.farmacia_medicitas.BuildConfig

fun ensureAbsoluteUrl(path: String, baseUrl: String = BuildConfig.BASE_URL): String {
    return if (path.startsWith("http")) {
        path
    } else {
        val origin = baseUrl
        val base = if (origin.endsWith("/")) origin.dropLast(1) else origin
        if (path.startsWith("/")) "$base$path" else "$base/$path"
    }
}

