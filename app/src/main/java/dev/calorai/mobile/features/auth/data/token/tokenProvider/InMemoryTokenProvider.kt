package dev.calorai.mobile.features.auth.data.token.tokenProvider

import dev.calorai.mobile.features.auth.data.token.TokenStorage

class InMemoryTokenProvider(
    private val tokenStorage: TokenStorage,
) : TokenProvider {

    private val lock = Object()

    override fun getAccessToken(): String? {
        synchronized(lock) {
            return tokenStorage.getAccessToken()
        }
    }
}
