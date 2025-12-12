package dev.calorai.mobile.features.auth.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.network.token.InMemoryTokenProvider
import dev.calorai.mobile.core.network.token.TokenProvider
import dev.calorai.mobile.features.auth.data.api.AuthApi
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
    single<TokenProvider> {
        InMemoryTokenProvider(get())
    }
}
