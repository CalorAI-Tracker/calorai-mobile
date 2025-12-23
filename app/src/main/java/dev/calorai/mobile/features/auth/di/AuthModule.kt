package dev.calorai.mobile.features.auth.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.network.di.RETROFIT_AUTH
import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.token.TokenRefresher
import dev.calorai.mobile.features.auth.data.token.TokenStorage
import dev.calorai.mobile.features.auth.data.token.tokenProvider.InMemoryTokenProvider
import dev.calorai.mobile.features.auth.data.token.tokenProvider.TokenProvider
import dev.calorai.mobile.features.auth.domain.UserHasAuthorizedUseCase
import dev.calorai.mobile.features.auth.domain.UserHasAuthorizedUseCaseImpl
import dev.calorai.mobile.features.auth.login.ui.LoginViewModel
import dev.calorai.mobile.features.auth.signUp.ui.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

internal val authModule = module {

    viewModel {
        LoginViewModel(
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
    viewModel {
        SignUpViewModel(
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
    single { get<Retrofit>(named(RETROFIT_AUTH)).create(AuthApi::class.java) }
    single {
        TokenStorage(
            context = androidContext()
        )
    }
    single {
        TokenRefresher(
            tokenStorage = get(),
            authApi = get()
        )
    }
    single<TokenProvider> {
        InMemoryTokenProvider(
            tokenStorage = get()
        )
    }
    factory<UserHasAuthorizedUseCase> {
        UserHasAuthorizedUseCaseImpl(
            tokenProvider = get(),
        )
    }
}
