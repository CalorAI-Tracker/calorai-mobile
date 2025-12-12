package dev.calorai.mobile.core.network.token

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator(
    private val tokenProvider: TokenProvider
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) return null

        val refreshed = tokenProvider.refreshTokenBlocking()
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