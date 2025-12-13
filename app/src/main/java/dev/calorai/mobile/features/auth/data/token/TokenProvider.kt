package dev.calorai.mobile.features.auth.data.token

interface TokenProvider {
    fun getAccessToken(): String?
    fun setTokens(accessToken: String, refreshToken: String)
    fun clearTokens()
    fun refreshTokenBlocking(): Boolean
}