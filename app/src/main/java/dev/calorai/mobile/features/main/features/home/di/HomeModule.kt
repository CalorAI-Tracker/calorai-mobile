package dev.calorai.mobile.features.main.features.home.di

import dev.calorai.mobile.features.main.features.home.domain.GetCurrentUserNameUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentUserNameUseCaseImpl
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentWeekUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetCurrentWeekUseCaseImpl
import dev.calorai.mobile.features.main.features.home.domain.GetMealsForDayUseCase
import dev.calorai.mobile.features.main.features.home.domain.GetMealsForDayUseCaseImpl
import dev.calorai.mobile.features.main.features.home.ui.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import java.time.Clock

internal val homeModule = module {
    factory<GetCurrentWeekUseCase> {
        GetCurrentWeekUseCaseImpl(
            androidContext(),
            clock = Clock.systemDefaultZone(),
        )
    }
    factory<GetCurrentUserNameUseCase> {
        GetCurrentUserNameUseCaseImpl()
    }
    factory<GetMealsForDayUseCase> {
        GetMealsForDayUseCaseImpl()
    }
    viewModelOf(::HomeViewModel)
}
