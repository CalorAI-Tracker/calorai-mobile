package dev.calorai.mobile.core.network.token

interface TokenProvider {
    fun getAccessToken(): String?
    fun setTokens(accessToken: String, refreshToken: String)
    fun clearTokens()
    fun refreshTokenBlocking(): Boolean
}