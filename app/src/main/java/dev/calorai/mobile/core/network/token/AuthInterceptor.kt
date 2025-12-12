package dev.calorai.mobile.core.network.token

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = tokenProvider.getAccessToken()
        return if (token.isNullOrBlank()) {
            chain.proceed(original)
        } else {
            val newReq = original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(newReq)
        }
    }
}