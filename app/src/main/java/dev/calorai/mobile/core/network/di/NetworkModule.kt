package dev.calorai.mobile.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.calorai.mobile.core.network.token.AuthInterceptor
import dev.calorai.mobile.core.network.token.TokenAuthenticator
import dev.calorai.mobile.core.network.token.TokenProvider
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

private const val BASE_URL = "http://localhost:8080/api/"

internal val networkModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }

    single(named("okHttpAuth")) {
        OkHttpClient.Builder()
            .build()
    }

    single(named("retrofitAuth")) {
        val json: Json = get()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named("okHttpAuth")))
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

    // OkHttp клиент для авторизованных запросов — добавляем Interceptor + Authenticator
    single(named("okHttpAuthorized")) {
        val tokenProvider: TokenProvider = get() // должен быть предоставлен в другом модуле до этого
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenProvider))       // добавляет Bearer
            .authenticator(TokenAuthenticator(tokenProvider))     // обрабатывает 401 и делает refresh
            .build()
    }

    single(named("retrofitAuthorized")) {
        val json: Json = get()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named("okHttpAuthorized")))
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
    }

}