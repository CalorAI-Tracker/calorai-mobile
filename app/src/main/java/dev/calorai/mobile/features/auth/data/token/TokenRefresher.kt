package dev.calorai.mobile.features.auth.data.token

import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshRequest
import kotlinx.coroutines.runBlocking

class TokenRefresher(
    private val authApi: AuthApi,
    private val tokenStorage: TokenStorage,
) {

    private var isRefreshing = false
    private val lock = Object()

    fun refreshTokenBlocking(): Boolean {
        synchronized(lock) {
            if (isRefreshing) {
                return false
            }
            isRefreshing = true
        }

        return try {
            val refreshToken = tokenStorage.getRefreshToken()
                ?: return false
            val response = try {
                runBlocking { authApi.refresh(RefreshRequest(refreshToken)) }
            } catch (e: Exception) {
                null
            }
            val responseBody = response?.body()
            if (
                response != null &&
                response.isSuccessful &&
                responseBody != null &&
                responseBody.accessToken.isNotBlank()
                ) {
                tokenStorage.setTokens(responseBody.accessToken, responseBody.refreshToken)
                true
            } else {
                tokenStorage.clearTokens()
                false
            }
        } finally {
            synchronized(lock) {
                isRefreshing = false
            }
        }
    }
}
