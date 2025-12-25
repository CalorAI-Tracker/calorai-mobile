package dev.calorai.mobile.features.auth.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.network.di.NetworkContext
import dev.calorai.mobile.features.auth.data.api.AuthApi
import dev.calorai.mobile.features.auth.data.mapper.AuthMapper
import dev.calorai.mobile.features.auth.data.repository.AuthRepositoryImpl
import dev.calorai.mobile.features.auth.data.token.CredentialsStore
import dev.calorai.mobile.features.auth.data.token.CredentialsStoreImpl
import dev.calorai.mobile.features.auth.data.token.InMemoryTokenProvider
import dev.calorai.mobile.features.auth.data.token.TokenProvider
import dev.calorai.mobile.features.auth.domain.AuthRepository
import dev.calorai.mobile.features.auth.domain.LoginUseCase
import dev.calorai.mobile.features.auth.domain.LoginUseCaseImpl
import dev.calorai.mobile.features.auth.domain.RefreshTokensUseCase
import dev.calorai.mobile.features.auth.domain.RefreshTokensUseCaseImpl
import dev.calorai.mobile.features.auth.domain.SignUpUseCase
import dev.calorai.mobile.features.auth.domain.SignUpUseCaseImpl
import dev.calorai.mobile.features.auth.domain.UserHasAuthorizedUseCase
import dev.calorai.mobile.features.auth.domain.UserHasAuthorizedUseCaseImpl
import dev.calorai.mobile.features.auth.login.ui.LoginViewModel
import dev.calorai.mobile.features.auth.signUp.ui.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

internal val authModule = module {

    viewModel {
        LoginViewModel(
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
            loginUseCase = get(),
        )
    }
    viewModel {
        SignUpViewModel(
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
            signUpUseCase = get(),
        )
    }
    single<AuthApi> { get<Retrofit>(qualifier<NetworkContext.Base>()).create(AuthApi::class.java) }
    single<CredentialsStore> {
        CredentialsStoreImpl(
            context = androidContext()
        )
    }
    single<RefreshTokensUseCase> {
        RefreshTokensUseCaseImpl(
            credentialsStore = get(),
            authApi = get()
        )
    }
    single<TokenProvider> {
        InMemoryTokenProvider(
            credentialsStore = get()
        )
    }
    factory { AuthMapper() }
    factory<AuthRepository> {
        AuthRepositoryImpl(
            api = get(),
            credentialsStore = get(),
            userIdStore = get(),
            userDao = get(),
            mapper = get(),
        )
    }
    factory<UserHasAuthorizedUseCase> {
        UserHasAuthorizedUseCaseImpl(
            userIdStore = get(),
        )
    }
    factory<LoginUseCase> {
        LoginUseCaseImpl(
            authRepository = get(),
            deviceIdStore = get(),
        )
    }
    factory<RefreshTokensUseCase> {
        RefreshTokensUseCaseImpl(
            authApi = get(),
            credentialsStore = get(),
        )
    }
    factory<SignUpUseCase> {
        SignUpUseCaseImpl(
            authRepository = get(),
        )
    }
}
