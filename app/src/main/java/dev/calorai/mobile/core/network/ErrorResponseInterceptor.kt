package dev.calorai.mobile.core.network

import android.util.Log
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class ErrorResponseInterceptor(
    private val json: Json
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val responseBody = response.body ?: return response
        val mediaType = responseBody.contentType() ?: "application/json; charset=utf-8".toMediaType()
        val responseJsonString = responseBody.string()
        try {
            val responseJson = json.parseToJsonElement(responseJsonString)
            val statusCode = responseJson.jsonObject["statusCode"]?.jsonPrimitive?.intOrNull
            if (statusCode != null) {
                Log.d("ErrorInterceptor", "Intercepted API statusCode: $statusCode")
            }
            if (statusCode != null && statusCode >= 400) {
                val newBody = responseJsonString.toResponseBody(mediaType)
                return response.newBuilder()
                    .code(statusCode)
                    .body(newBody)
                    .message(responseJson.jsonObject["message"]?.jsonPrimitive?.contentOrNull ?: response.message)
                    .build()
            }
        } catch (parseException: Exception) {
            Log.w("ErrorResponseInterceptor", "Не удалось распарсить тело ответа как JSON: $parseException")
            val restoredBody = responseJsonString.toResponseBody(mediaType)
            return response.newBuilder().body(restoredBody).build()
        }
        val restoredBody = responseJsonString.toResponseBody(mediaType)
        return response.newBuilder().body(restoredBody).build()
    }
}
