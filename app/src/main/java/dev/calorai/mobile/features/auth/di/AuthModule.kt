package dev.calorai.mobile.features.auth.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.auth.data.token.AuthInterceptor
import dev.calorai.mobile.features.auth.data.token.InMemoryTokenProvider
import dev.calorai.mobile.features.auth.data.token.TokenAuthenticator
import dev.calorai.mobile.features.auth.data.token.TokenProvider
import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.token.TokenStorage
import dev.calorai.mobile.features.auth.ui.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

internal val authModule = module {
    viewModel {
        AuthViewModel(
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }

    single { get<Retrofit>(named("retrofitAuth")).create(AuthApi::class.java) }
    single { TokenStorage(get()) }
    single<TokenProvider> {
        InMemoryTokenProvider(get(), get())
    }
    single { AuthInterceptor(get()) }
    single { TokenAuthenticator(get()) }
}
