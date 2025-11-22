package dev.calorai.mobile.features.main.features.home.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentUserNameUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentUserNameUseCaseImpl
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentWeekUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentWeekUseCaseImpl
import dev.calorai.mobile.features.main.features.home.domain.GetMealsForDayUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetMealsForDayUseCaseImpl
import dev.calorai.mobile.features.main.features.home.ui.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import java.time.Clock

internal val homeModule = module {
    factory<GetCurrentWeekUseCase> {
        GetCurrentWeekUseCaseImpl(
            context = androidContext(),
            clock = Clock.systemDefaultZone(),
        )
    }
    factory<GetCurrentUserNameUseCase> {
        GetCurrentUserNameUseCaseImpl()
    }
    factory<GetMealsForDayUseCase> {
        GetMealsForDayUseCaseImpl()
    }
    viewModel {
        HomeViewModel(
            getCurrentWeekUseCase = get<GetCurrentWeekUseCase>(),
            getCurrentUserNameUseCase = get<GetCurrentUserNameUseCase>(),
            getMealsForDayUseCase = get<GetMealsForDayUseCase>(),
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
}
