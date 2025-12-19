package dev.calorai.mobile.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.calorai.mobile.BuildConfig
import dev.calorai.mobile.core.network.ErrorResponseInterceptor
import dev.calorai.mobile.features.auth.data.token.AuthInterceptor
import dev.calorai.mobile.features.auth.data.token.TokenAuthenticator
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

private const val BASE_URL = BuildConfig.BASE_URL

private const val OKHTTP_AUTH = "okHttpAuth"
private const val OKHTTP_AUTHORIZED = "okHttpAuthorized"
private const val RETROFIT_AUTH = "retrofitAuth"
private const val RETROFIT_AUTHORIZED = "retrofitAuthorized"


internal val networkModule = module {

    single {
        Json {
            ignoreUnknownKeys = true
        }
    }

    single(named(OKHTTP_AUTH)) {
        OkHttpClient.Builder()
            .addNetworkInterceptor(ErrorResponseInterceptor(json = get()))
            .build()
    }

    single(named(RETROFIT_AUTH)) {
        val json: Json = get()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named(OKHTTP_AUTH)))
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=utf-8".toMediaType())
            )
            .build()
    }

    single(named(OKHTTP_AUTHORIZED)) {
        OkHttpClient.Builder()
            .addNetworkInterceptor(ErrorResponseInterceptor(json = get()))
            .addInterceptor(
                AuthInterceptor(
                    tokenProvider = get()
                )
            )
            .authenticator(
                TokenAuthenticator(
                    tokenProvider = get(),
                    tokenRefresher = get(),
                )
            )
            .build()
    }

    single(named(RETROFIT_AUTHORIZED)) {
        val json: Json = get()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get(named(OKHTTP_AUTHORIZED)))
            .addConverterFactory(
                json.asConverterFactory("application/json; charset=utf-8".toMediaType())
            )
            .build()
    }
}
