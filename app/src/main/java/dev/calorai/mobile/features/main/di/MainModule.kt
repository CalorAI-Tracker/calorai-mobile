package dev.calorai.mobile.features.main.di

import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.navigation.RouterControllerImpl
import dev.calorai.mobile.features.main.MainRouterContext
import dev.calorai.mobile.features.main.ui.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val mainModule = module {
    single<RouterController>(qualifier<MainRouterContext>()) { RouterControllerImpl() }
    viewModel {
        MainViewModel(
            mainRouter = get<RouterController>(qualifier<MainRouterContext>()),
        )
    }
}
