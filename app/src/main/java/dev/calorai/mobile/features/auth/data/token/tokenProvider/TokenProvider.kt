package dev.calorai.mobile.features.auth.data.token.tokenProvider

interface TokenProvider {

    fun getAccessToken(): String?
}
