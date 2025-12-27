package dev.calorai.mobile.features.auth.data.token

import dev.calorai.mobile.features.auth.data.BEARER_HEADER
import dev.calorai.mobile.features.auth.data.toBearerHeader
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor constructor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = runBlocking { tokenProvider.getAccessToken() }
        val requestToSend = original.newBuilder().apply {
            if (!token?.value.isNullOrBlank()) {
                addHeader(BEARER_HEADER, token.toBearerHeader())
            }
        }.build()
        return chain.proceed(requestToSend)
    }
}
