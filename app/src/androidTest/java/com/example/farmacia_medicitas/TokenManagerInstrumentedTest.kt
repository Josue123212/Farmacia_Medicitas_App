package com.example.farmacia_medicitas

import androidx.test.platform.app.InstrumentationRegistry
import com.example.farmacia_medicitas.data.auth.TokenManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TokenManagerInstrumentedTest {
    @Test
    fun save_and_get_and_clear_tokens_work() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tm = TokenManager(context)

        // Ensure clean state
        tm.clear()
        assertNull(tm.getAccessToken())
        assertNull(tm.getRefreshToken())

        tm.saveTokens(access = "ACCESS_X", refresh = "REFRESH_Y")
        assertEquals("ACCESS_X", tm.getAccessToken())
        assertEquals("REFRESH_Y", tm.getRefreshToken())

        // Update access only
        tm.setAccessTokenForTest("ACCESS_NEW")
        assertEquals("ACCESS_NEW", tm.getAccessToken())
        assertEquals("REFRESH_Y", tm.getRefreshToken())

        // Clear
        tm.clear()
        assertNull(tm.getAccessToken())
        assertNull(tm.getRefreshToken())
    }
}