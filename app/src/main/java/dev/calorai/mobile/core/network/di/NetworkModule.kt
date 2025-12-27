package dev.calorai.mobile.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dev.calorai.mobile.BuildConfig
import dev.calorai.mobile.core.network.ErrorResponseInterceptor
import dev.calorai.mobile.features.auth.data.token.AuthInterceptor
import dev.calorai.mobile.features.auth.data.token.TokenAuthenticator
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

sealed interface NetworkContext {
    data object Base : NetworkContext
    data object Authorized : NetworkContext
}

internal val networkModule = module {

    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
    }

    single<OkHttpClient>(qualifier<NetworkContext.Base>()) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addNetworkInterceptor(ErrorResponseInterceptor(json = get()))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    single<Retrofit>(qualifier<NetworkContext.Base>()) {
        val jsonConverterFactory = get<Json>().asConverterFactory(
            contentType = "application/json; charset=utf-8".toMediaType()
        )
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get<OkHttpClient>(qualifier<NetworkContext.Base>()))
            .addConverterFactory(jsonConverterFactory)
            .build()
    }

    single<OkHttpClient>(qualifier<NetworkContext.Authorized>()) {
        get<OkHttpClient>(qualifier<NetworkContext.Base>()).newBuilder()
            .addInterceptor(
                AuthInterceptor(
                    tokenProvider = get()
                )
            )
            .authenticator(
                TokenAuthenticator(
                    tokenProvider = get(),
                    refreshTokensUseCase = get(),
                )
            )
            .build()
    }

    single<Retrofit>(qualifier<NetworkContext.Authorized>()) {
        get<Retrofit>(qualifier<NetworkContext.Base>()).newBuilder()
            .client(get<OkHttpClient>(qualifier<NetworkContext.Authorized>()))
            .build()
    }
}
