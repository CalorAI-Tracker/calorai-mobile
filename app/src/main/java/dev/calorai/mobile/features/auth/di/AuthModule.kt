package dev.calorai.mobile.features.auth.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.auth.ui.AuthViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val authModule = module {
    viewModel {
        AuthViewModel(
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
}
