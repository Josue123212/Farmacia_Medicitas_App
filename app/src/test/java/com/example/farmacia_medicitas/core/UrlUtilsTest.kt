package com.example.farmacia_medicitas.core

import org.junit.Assert.assertEquals
import org.junit.Test

class UrlUtilsTest {
    @Test
    fun ensuresAbsoluteUrlWithLeadingSlash() {
        val base = "http://example.com/"
        val path = "/media/image.png"
        assertEquals("http://example.com/media/image.png", ensureAbsoluteUrl(path, base))
    }

    @Test
    fun ensuresAbsoluteUrlWithoutLeadingSlash() {
        val base = "http://example.com/"
        val path = "media/image.png"
        assertEquals("http://example.com/media/image.png", ensureAbsoluteUrl(path, base))
    }

    @Test
    fun returnsAbsoluteUrlUntouched() {
        val base = "http://example.com/"
        val path = "https://cdn.com/img.png"
        assertEquals("https://cdn.com/img.png", ensureAbsoluteUrl(path, base))
    }

    @Test
    fun trimsBaseTrailingSlash() {
        val base = "http://example.com/"
        val path = "/a/b"
        assertEquals("http://example.com/a/b", ensureAbsoluteUrl(path, base))
    }
}

