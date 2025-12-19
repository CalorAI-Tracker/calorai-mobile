package dev.calorai.mobile.features.auth.data.token

import dev.calorai.mobile.features.auth.data.token.tokenProvider.TokenProvider
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenProvider: TokenProvider,
    private val tokenRefresher: TokenRefresher
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val refreshed = tokenRefresher.refreshTokenBlocking()
        if (!refreshed) {
            return null
        }

        val newAccess = tokenProvider.getAccessToken() ?: return null
        return response.request.newBuilder()
            .header("Authorization", "Bearer $newAccess")
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
