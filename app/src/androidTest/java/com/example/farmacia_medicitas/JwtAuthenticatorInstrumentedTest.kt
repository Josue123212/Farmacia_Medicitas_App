package com.example.farmacia_medicitas

import androidx.test.platform.app.InstrumentationRegistry
import com.example.farmacia_medicitas.data.auth.TokenManager
import com.example.farmacia_medicitas.data.remote.ApiService
import com.example.farmacia_medicitas.data.remote.auth.JwtAuthenticator
import com.example.farmacia_medicitas.data.remote.dto.PaginatedResponse
import com.example.farmacia_medicitas.data.remote.dto.ProductDto
import com.example.farmacia_medicitas.data.remote.dto.auth.LoginEnvelope
import com.example.farmacia_medicitas.data.remote.dto.auth.LoginRequest
import com.example.farmacia_medicitas.data.remote.dto.auth.RefreshRequest
import com.example.farmacia_medicitas.data.remote.dto.auth.RefreshResponse
import com.example.farmacia_medicitas.data.remote.dto.auth.UserDto
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Instrumented tests for JwtAuthenticator behavior on 401 responses.
 */
class JwtAuthenticatorInstrumentedTest {

    private class FakeApiService(private val onRefresh: (RefreshRequest) -> RefreshResponse) : ApiService {
        override suspend fun getProducts(page: Int?, pageSize: Int?, search: String?, categoryId: Int?): PaginatedResponse<ProductDto> {
            throw NotImplementedError()
        }

        override suspend fun getProduct(id: Int): ProductDto {
            throw NotImplementedError()
        }

        override suspend fun login(body: LoginRequest): LoginEnvelope {
            throw NotImplementedError()
        }

        override suspend fun refresh(body: RefreshRequest): RefreshResponse {
            return onRefresh(body)
        }

        override suspend fun me(): UserDto {
            throw NotImplementedError()
        }
    }

    private fun make401ResponseWithAuth(access: String): Response {
        val request: Request = Request.Builder()
            .url("https://example.test/api")
            .header("Authorization", "Bearer $access")
            .build()
        return Response.Builder()
            .code(401)
            .message("Unauthorized")
            .protocol(Protocol.HTTP_1_1)
            .request(request)
            .build()
    }

    @Test
    fun authenticate_refreshes_token_and_updates_request_header() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tm = TokenManager(context)
        tm.clear()
        tm.saveTokens(access = "OLD_ACCESS", refresh = "REFRESH_TOKEN")

        val api = FakeApiService { req ->
            // Assert we are using the stored refresh token
            assertEquals("REFRESH_TOKEN", req.refresh)
            RefreshResponse(access = "NEW_ACCESS")
        }
        val auth = JwtAuthenticator(api, tm)

        val response401 = make401ResponseWithAuth(access = "OLD_ACCESS")
        val newRequest = auth.authenticate(null, response401)

        // Should produce a new request with the new Authorization header
        requireNotNull(newRequest)
        assertEquals("Bearer NEW_ACCESS", newRequest.header("Authorization"))
        // TokenManager should now hold the new access token while keeping the same refresh
        assertEquals("NEW_ACCESS", tm.getAccessToken())
        assertEquals("REFRESH_TOKEN", tm.getRefreshToken())
    }

    @Test
    fun authenticate_clears_tokens_on_refresh_failure_and_returns_null() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tm = TokenManager(context)
        tm.clear()
        tm.saveTokens(access = "OLD_ACCESS", refresh = "REFRESH_TOKEN")

        val api = FakeApiService { _ ->
            throw RuntimeException("refresh failed")
        }
        val auth = JwtAuthenticator(api, tm)

        val response401 = make401ResponseWithAuth(access = "OLD_ACCESS")
        val newRequest = auth.authenticate(null, response401)

        // On failure, authenticate should return null and tokens should be cleared
        assertNull(newRequest)
        assertNull(tm.getAccessToken())
        assertNull(tm.getRefreshToken())
    }
}