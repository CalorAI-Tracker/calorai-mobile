package dev.calorai.mobile.features.main.features.home.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.main.features.home.domain.CheckIsFirstDayOfWeekUseCase
import dev.calorai.mobile.features.main.features.home.domain.CheckIsFirstDayOfWeekUseCaseImpl
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentUserNameUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentUserNameUseCaseImpl
import dev.calorai.mobile.features.main.features.home.domain.GetWeekByDateUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetWeekByDateUseCaseImpl
import dev.calorai.mobile.features.main.features.home.domain.GetMealsForDayUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetMealsForDayUseCaseImpl
import dev.calorai.mobile.features.main.features.home.ui.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val homeModule = module {
    factory<GetWeekByDateUseCase> {
        GetWeekByDateUseCaseImpl(androidContext())
    }
    factory<CheckIsFirstDayOfWeekUseCase> {
        CheckIsFirstDayOfWeekUseCaseImpl(androidContext())
    }
    factory<GetCurrentUserNameUseCase> {
        GetCurrentUserNameUseCaseImpl()
    }
    factory<GetMealsForDayUseCase> {
        GetMealsForDayUseCaseImpl()
    }
    viewModel {
        HomeViewModel(
            getWeekByDateUseCase = get<GetWeekByDateUseCase>(),
            getCurrentUserNameUseCase = get<GetCurrentUserNameUseCase>(),
            getMealsForDayUseCase = get<GetMealsForDayUseCase>(),
            checkIsFirstDayOfWeekUseCase = get<CheckIsFirstDayOfWeekUseCase>(),
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
}
