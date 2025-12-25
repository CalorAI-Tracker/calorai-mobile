package dev.calorai.mobile.features.auth.data.token

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import dev.calorai.mobile.features.auth.data.AccessToken

interface CredentialsStore {

    suspend fun getRefreshToken(): String?
    suspend fun getAccessToken(): AccessToken?

    suspend fun setCredentials(
        accessToken: String,
        refreshToken: String,
    )

    fun clearCredentials()
}

class CredentialsStoreImpl constructor(
    context: Context
) : CredentialsStore {

    companion object {
        private const val KEY_ACCESS = "access_token"
        private const val KEY_REFRESH = "refresh_token"
        private const val PREFS_NAME = "token_prefs"
    }

    private val prefs by lazy { context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE) }

    @Volatile
    private var inMemoryAccessToken: String? = prefs.getString(KEY_ACCESS, null)

    override suspend fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH, null)
    }

    override suspend fun getAccessToken(): AccessToken? {
        return inMemoryAccessToken?.let { AccessToken(it) }
    }

    override suspend fun setCredentials(accessToken: String, refreshToken: String) {
        inMemoryAccessToken = accessToken
        prefs.edit().apply {
            putString(KEY_ACCESS, accessToken)
            putString(KEY_REFRESH, refreshToken)
            apply()
        }
    }

    override fun clearCredentials() {
        prefs.edit { clear() }
        inMemoryAccessToken = null
    }
}
