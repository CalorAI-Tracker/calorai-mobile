package dev.calorai.mobile.features.splash.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.splash.ui.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val splashModule = module {
    viewModel {
        SplashViewModel(
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
            userHasAuthorizedUseCase = get(),
            refreshTokensUseCase = get(),
        )
    }
}
