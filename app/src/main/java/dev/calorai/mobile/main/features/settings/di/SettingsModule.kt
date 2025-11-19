package dev.calorai.mobile.main.features.settings.di

import dev.calorai.mobile.main.features.settings.ui.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val settingsModule = module {
    viewModelOf(::SettingsViewModel)
}
