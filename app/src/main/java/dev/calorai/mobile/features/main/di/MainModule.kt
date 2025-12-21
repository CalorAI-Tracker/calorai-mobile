package dev.calorai.mobile.features.main.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.navigation.RouterControllerImpl
import dev.calorai.mobile.core.network.di.RETROFIT_AUTHORIZED
import dev.calorai.mobile.features.main.MainRouterContext
import dev.calorai.mobile.features.main.data.api.DailyNutritionApi
import dev.calorai.mobile.features.main.data.api.UserProfileApi
import dev.calorai.mobile.features.main.features.home.di.homeModule
import dev.calorai.mobile.features.main.features.plan.di.planModule
import dev.calorai.mobile.features.main.features.progress.di.progressModule
import dev.calorai.mobile.features.main.features.settings.di.settingsModule
import dev.calorai.mobile.features.main.ui.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

internal val mainModule = module {
    single<RouterController>(qualifier<MainRouterContext>()) { RouterControllerImpl() }
    viewModel {
        MainViewModel(
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
            mainRouter = get<RouterController>(qualifier<MainRouterContext>()),
        )
    }

    single { get<Retrofit>(named(RETROFIT_AUTHORIZED)).create(UserProfileApi::class.java) }
    single { get<Retrofit>(named(RETROFIT_AUTHORIZED)).create(DailyNutritionApi::class.java) }

    includes(
        homeModule,
        planModule,
        progressModule,
        settingsModule,
    )
}
