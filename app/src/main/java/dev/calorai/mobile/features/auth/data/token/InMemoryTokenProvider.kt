package dev.calorai.mobile.features.auth.data.token

import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshRequest
import kotlinx.coroutines.runBlocking

class InMemoryTokenProvider(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage,
) : TokenProvider{
    @Volatile private var accessToken: String? = tokenStorage.getAccessToken()
    @Volatile private var refreshToken: String? = tokenStorage.getRefreshToken()
    @Volatile private var isRefreshing = false

    private val lock = Object()

    override fun getAccessToken(): String? = accessToken

    override fun setTokens(
        accessToken: String,
        refreshToken: String
    ) {
        synchronized(lock) {
            this.accessToken = accessToken
            this.refreshToken = refreshToken
            tokenStorage.setTokens(accessToken, refreshToken)
        }
    }

    override fun clearTokens() {
        synchronized(lock) {
            accessToken = null
            refreshToken = null
            tokenStorage.clearTokens()
        }
    }

    override fun refreshTokenBlocking(): Boolean {
        val currentRefresh = refreshToken ?: return false

        synchronized(lock) {
            if (isRefreshing) {
                try {
                    while (isRefreshing) lock.wait()
                } catch (e: InterruptedException) {
                    return false
                }
                return !accessToken.isNullOrBlank()
            }
            isRefreshing = true
        }

        return try {
            val response = try {
                runBlocking { authApi.refresh(RefreshRequest(currentRefresh)) }
            } catch (e: Exception) {
                null
            }

            if (response != null && response.accessToken.isNotBlank()) {
                setTokens(response.accessToken, response.refreshToken)
                true
            } else {
                clearTokens()
                false
            }
        } finally {
            synchronized(lock) {
                isRefreshing = false
                lock.notifyAll()
            }
        }
    }
}