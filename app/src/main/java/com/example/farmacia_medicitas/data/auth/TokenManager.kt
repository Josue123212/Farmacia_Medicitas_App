package com.example.farmacia_medicitas.data.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import com.example.farmacia_medicitas.BuildConfig

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs: SharedPreferences = if (BuildConfig.DEBUG) {
        // En debug: usar SharedPreferences normales para facilitar pruebas y depuración
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    } else {
        // En release: usar EncryptedSharedPreferences
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveTokens(access: String, refresh: String) {
        // Usar commit() para asegurar que el guardado sea síncrono antes de que otras pantallas lean el token
        // Esto evita condiciones de carrera al navegar inmediatamente al Checkout tras login
        prefs.edit()
            .putString(KEY_ACCESS, access)
            .putString(KEY_REFRESH, refresh)
            .commit()
    }

    fun getAccessToken(): String? = prefs.getString(KEY_ACCESS, null)

    fun getRefreshToken(): String? = prefs.getString(KEY_REFRESH, null)

    // Útil para pruebas: actualizar solo el access
    fun setAccessTokenForTest(access: String) {
        val refresh = getRefreshToken()
        prefs.edit()
            .putString(KEY_ACCESS, access)
            .commit()
        // Mantiene el refresh existente
        if (refresh != null) {
            prefs.edit().putString(KEY_REFRESH, refresh).commit()
        }
    }

    fun clear() {
        // Limpiar de forma síncrona para que el estado de autenticación sea coherente de inmediato
        prefs.edit().clear().commit()
    }

    companion object {
        private const val PREFS_NAME = "secure_tokens"
        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"
    }
}