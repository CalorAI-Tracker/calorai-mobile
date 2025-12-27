package dev.calorai.mobile.features.auth.data.token

import dev.calorai.mobile.features.auth.data.BEARER_HEADER
import dev.calorai.mobile.features.auth.data.toBearerHeader
import dev.calorai.mobile.features.auth.domain.RefreshTokensUseCase
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator constructor(
    private val tokenProvider: TokenProvider,
    private val refreshTokensUseCase: RefreshTokensUseCase,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val refreshResult = runCatching { runBlocking { refreshTokensUseCase.invoke() } }
        if (refreshResult.isFailure) return null

        val newAccess = runBlocking { tokenProvider.getAccessToken() } ?: return null
        return response.request.newBuilder()
            .header(BEARER_HEADER, newAccess.toBearerHeader())
            .build()
    }

    private fun responseCount(response: Response): Int {
        var res: Response? = response
        var result = 1
        while (res?.priorResponse != null) {
            result++
            res = res.priorResponse
        }
        return result
    }
}
