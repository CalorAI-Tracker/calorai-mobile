package dev.calorai.mobile.features.main.features.settings.di

import dev.calorai.mobile.features.main.features.settings.data.FakeSettingsRepository
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
    single<SettingsRepository> { FakeSettingsRepository() }
    factory { SettingsMapper(androidContext()) }
    factory<UpdateUserHealthProfileUseCase> {
        UpdateUserHealthProfileUseCaseImpl(get())
    }
    factory<GetUserProfileUseCase> {
        GetUserProfileUseCaseImpl(get())
    }
    viewModelOf(::SettingsViewModel)
}
