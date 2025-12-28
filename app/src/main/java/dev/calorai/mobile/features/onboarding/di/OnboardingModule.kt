package dev.calorai.mobile.features.onboarding.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.onboarding.data.OnboardingMapper
import dev.calorai.mobile.features.onboarding.ui.OnboardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val onboardingModule = module {

    viewModel {
        OnboardingViewModel(
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
            savedStateHandle = get(),
            createUserProfileUseCase = get(),
            mapper = get(),
        )
    }
    factory<OnboardingMapper> { OnboardingMapper(context = androidContext()) }
}
