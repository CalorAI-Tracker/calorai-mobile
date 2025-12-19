package dev.calorai.mobile.features.auth.data.token

import android.util.Log
import dev.calorai.mobile.features.auth.data.token.tokenProvider.TokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val tokenProvider: TokenProvider
) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = tokenProvider.getAccessToken()
        val requestToSend = if (token.isNullOrBlank()) {
            original
        } else {
            original.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        }
        Log.d(TAG, "Sending request: ${requestToSend.method} ${requestToSend.url}")
        Log.d(TAG, "Request headers: ${requestToSend.headers}")
        val response = chain.proceed(requestToSend)
        Log.d(TAG, "Received response: ${response.code} for ${requestToSend.url}")
        Log.d(TAG, "Response headers: ${response.headers}")
        return response
    }
}
