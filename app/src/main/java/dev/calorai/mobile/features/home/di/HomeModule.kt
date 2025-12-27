package dev.calorai.mobile.features.home.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.network.di.NetworkContext
import dev.calorai.mobile.features.home.data.api.DailyNutritionApi
import dev.calorai.mobile.features.home.domain.usecases.GetCurrentUserNameUseCase
import dev.calorai.mobile.features.home.domain.usecases.GetCurrentUserNameUseCaseImpl
import dev.calorai.mobile.features.home.domain.usecases.GetDayProgressUseCase
import dev.calorai.mobile.features.home.domain.usecases.GetDayProgressUseCaseImpl
import dev.calorai.mobile.features.home.domain.usecases.GetWeekByDateUseCase
import dev.calorai.mobile.features.home.domain.usecases.GetWeekByDateUseCaseImpl
import dev.calorai.mobile.features.home.ui.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

internal val homeModule = module {

    single { get<Retrofit>(qualifier<NetworkContext.Authorized>()).create(DailyNutritionApi::class.java) }
    factory<GetWeekByDateUseCase> {
        GetWeekByDateUseCaseImpl(androidContext())
    }
    factory<GetCurrentUserNameUseCase> {
        GetCurrentUserNameUseCaseImpl(userDao = get())
    }
    factory<GetDayProgressUseCase> {
        GetDayProgressUseCaseImpl(repository = get())
    }
    viewModel {
        HomeViewModel(
            getWeekByDateUseCase = get<GetWeekByDateUseCase>(),
            getCurrentUserNameUseCase = get<GetCurrentUserNameUseCase>(),
            mapper = get(),
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
            getDayProgressUseCase = get(),
        )
    }
}
