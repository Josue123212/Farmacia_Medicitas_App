package com.example.farmacia_medicitas.data.remote.auth

import com.example.farmacia_medicitas.data.auth.TokenManager
import com.example.farmacia_medicitas.data.remote.ApiService
import com.example.farmacia_medicitas.data.remote.dto.auth.RefreshRequest
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * OkHttp Authenticator que, ante respuestas 401, intenta refrescar el token de acceso
 * utilizando el refresh token almacenado. Si el refresh es exitoso, rehace la solicitud
 * original con el nuevo Authorization: Bearer <access>.
 */
@Singleton
class JwtAuthenticator @Inject constructor(
    @Named("noAuth") private val apiServiceNoAuth: ApiService,
    private val tokenManager: TokenManager
) : Authenticator {

    private val refreshMutex = Mutex()

    override fun authenticate(route: Route?, response: Response): Request? {
        // Evitar bucles de reintentos
        if (responseCount(response) >= 2) return null

        val currentAuthHeader = response.request.header("Authorization")

        val newAccess: String = runBlocking {
            refreshMutex.withLock {
                val refresh = tokenManager.getRefreshToken() ?: return@withLock null
                try {
                    val refreshed = apiServiceNoAuth.refresh(RefreshRequest(refresh))
                    // Guardar nuevo access y mantener el mismo refresh
                    tokenManager.saveTokens(refreshed.access, refresh)
                    refreshed.access
                } catch (e: Exception) {
                    // Si falla el refresh, limpiar tokens para forzar re-login
                    tokenManager.clear()
                    null
                }
            }
        } ?: return null

        val newAuthHeader = "Bearer $newAccess"
        // Si el request ya tenía el nuevo token, no reintentar
        if (currentAuthHeader == newAuthHeader) return null

        return response.request.newBuilder()
            .header("Authorization", newAuthHeader)
            .build()
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}