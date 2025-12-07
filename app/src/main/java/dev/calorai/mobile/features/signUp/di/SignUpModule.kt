package dev.calorai.mobile.features.signUp.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.signUp.ui.SignUpViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val signUpModule = module {
    viewModel {
        SignUpViewModel(
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
}
