package dev.calorai.mobile.core.network.token

import dev.calorai.mobile.features.auth.data.api.AuthApi

class InMemoryTokenProvider(
    private val authApi: AuthApi
) : TokenProvider{
    @Volatile private var accessToken: String? = null
    @Volatile private var refreshToken: String? = null

    private val lock = Any()

    override fun getAccessToken(): String? = accessToken

    override fun setTokens(accessToken: String, refreshToken: String) {
        synchronized(lock) {
            this.accessToken = accessToken
            this.refreshToken = refreshToken
        }
    }

    override fun clearTokens() {
        synchronized(lock) {
            accessToken = null
            refreshToken = null
        }
    }

    override fun refreshTokenBlocking(): Boolean {
        val currentRefresh: String = refreshToken ?: return false
        // Выполняем синхронный вызов через authApi (execute)
        return try {
            val response = authApi.refresh(RefreshRequest(currentRefresh)).execute()
            if (response.isSuccessful) {
                val body = response.body() ?: return false
                setTokens(body.accessToken, body.refreshToken)
                true
            } else {
                clearTokens()
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}