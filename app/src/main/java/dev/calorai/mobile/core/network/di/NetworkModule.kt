package dev.calorai.mobile.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.calorai.mobile.features.auth.data.token.AuthInterceptor
import dev.calorai.mobile.features.auth.data.token.TokenAuthenticator
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

    single(named("okHttpAuthorized")) {
        OkHttpClient.Builder()
            .addInterceptor(get<AuthInterceptor>())
            .authenticator(get<TokenAuthenticator>())
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