package dev.calorai.mobile.features.home.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.network.di.RETROFIT_AUTHORIZED
import dev.calorai.mobile.features.home.data.api.DailyNutritionApi
import dev.calorai.mobile.features.home.domain.CheckIsFirstDayOfWeekUseCase
import dev.calorai.mobile.features.home.domain.CheckIsFirstDayOfWeekUseCaseImpl
import dev.calorai.mobile.features.home.domain.GetCurrentUserNameUseCase
import dev.calorai.mobile.features.home.domain.GetCurrentUserNameUseCaseImpl
import dev.calorai.mobile.features.home.domain.GetMealsForDayUseCase
import dev.calorai.mobile.features.home.domain.GetMealsForDayUseCaseImpl
import dev.calorai.mobile.features.home.domain.GetPieChartsDataForDayUseCase
import dev.calorai.mobile.features.home.domain.GetPieChartsDataForDayUseCaseImpl
import dev.calorai.mobile.features.home.domain.GetWeekByDateUseCase
import dev.calorai.mobile.features.home.domain.GetWeekByDateUseCaseImpl
import dev.calorai.mobile.features.home.ui.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

internal val homeModule = module {

    single { get<Retrofit>(named(RETROFIT_AUTHORIZED)).create(DailyNutritionApi::class.java) }
    factory<GetWeekByDateUseCase> {
        GetWeekByDateUseCaseImpl(androidContext())
    }
    factory<CheckIsFirstDayOfWeekUseCase> {
        CheckIsFirstDayOfWeekUseCaseImpl(androidContext())
    }
    factory<GetCurrentUserNameUseCase> {
        GetCurrentUserNameUseCaseImpl(userDao = get())
    }
    factory<GetMealsForDayUseCase> {
        GetMealsForDayUseCaseImpl()
    }
    factory<GetPieChartsDataForDayUseCase> {
        GetPieChartsDataForDayUseCaseImpl()
    }
    viewModel {
        HomeViewModel(
            getWeekByDateUseCase = get<GetWeekByDateUseCase>(),
            getCurrentUserNameUseCase = get<GetCurrentUserNameUseCase>(),
            getMealsForDayUseCase = get<GetMealsForDayUseCase>(),
            getPieChartsDataForDayUseCase = get<GetPieChartsDataForDayUseCase>(),
            checkIsFirstDayOfWeekUseCase = get<CheckIsFirstDayOfWeekUseCase>(),
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
}
