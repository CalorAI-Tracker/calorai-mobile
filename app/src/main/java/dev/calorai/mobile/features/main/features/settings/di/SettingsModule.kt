package dev.calorai.mobile.features.main.features.settings.di

import dev.calorai.mobile.features.main.features.settings.data.SettingsRepositoryImpl
import dev.calorai.mobile.features.main.features.settings.data.SettingsMapper
import dev.calorai.mobile.features.main.features.settings.domain.GetUserProfileUseCase
import dev.calorai.mobile.features.main.features.settings.domain.GetUserProfileUseCaseImpl
import dev.calorai.mobile.features.main.features.settings.domain.SettingsRepository
import dev.calorai.mobile.features.main.features.settings.domain.UpdateUserHealthProfileUseCase
import dev.calorai.mobile.features.main.features.settings.domain.UpdateUserHealthProfileUseCaseImpl
import dev.calorai.mobile.features.main.features.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val settingsModule = module {
    factory { SettingsMapper(androidContext()) }
    single<SettingsRepository> {
        SettingsRepositoryImpl(
            userProfileApi = get(),
            userDao = get(),
            mapper = get(),
        )
    }
    factory<UpdateUserHealthProfileUseCase> {
        UpdateUserHealthProfileUseCaseImpl(repository = get())
    }
    factory<GetUserProfileUseCase> {
        GetUserProfileUseCaseImpl(repository = get())
    }
    viewModelOf(::SettingsViewModel)
}
