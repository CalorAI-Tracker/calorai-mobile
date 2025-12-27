package dev.calorai.mobile.features.auth.data.token

import dev.calorai.mobile.features.auth.data.AccessToken

interface TokenProvider {

    suspend fun getAccessToken(): AccessToken?
}

class InMemoryTokenProvider(
    private val credentialsStore: CredentialsStore,
) : TokenProvider {

    override suspend fun getAccessToken(): AccessToken? {
        return credentialsStore.getAccessToken()
    }
}
