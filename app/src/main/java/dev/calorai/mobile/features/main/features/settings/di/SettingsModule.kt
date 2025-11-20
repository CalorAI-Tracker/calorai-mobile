package dev.calorai.mobile.features.main.features.settings.di

import dev.calorai.mobile.features.main.features.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val settingsModule = module {
    viewModelOf(::SettingsViewModel)
}
