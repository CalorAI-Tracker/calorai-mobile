package dev.calorai.mobile.features.auth.domain

import dev.calorai.mobile.core.network.model.ServerHttpException
import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.dto.refresh.RefreshRequest
import dev.calorai.mobile.features.auth.data.token.CredentialsStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

interface RefreshTokensUseCase {

    suspend operator fun invoke()
}

class RefreshTokensUseCaseImpl constructor(
    private val authApi: AuthApi,
    private val credentialsStore: CredentialsStore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : RefreshTokensUseCase {

    private val mutex = Mutex()

    override suspend fun invoke() {
        withContext(dispatcher) {
            mutex.withLock {
                val refreshToken = credentialsStore.getRefreshToken()
                    ?: throw IllegalStateException("RT is empty")
                val response = authApi.refresh(RefreshRequest(refreshToken))
                if (!response.isSuccessful) throw ServerHttpException(response.message())
                val tokens = requireNotNull(response.body())
                credentialsStore.setCredentials(
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken,
                )
            }
        }
    }
}
