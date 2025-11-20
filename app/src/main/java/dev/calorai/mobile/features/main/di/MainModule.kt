package dev.calorai.mobile.features.main.di

import dev.calorai.mobile.features.main.features.home.di.homeModule
import dev.calorai.mobile.features.main.features.plan.di.planModule
import dev.calorai.mobile.features.main.features.progress.di.progressModule
import dev.calorai.mobile.features.main.features.settings.di.settingsModule
import dev.calorai.mobile.features.main.ui.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val mainModule = module {
    viewModelOf(::MainViewModel)
    includes(
        homeModule,
        planModule,
        progressModule,
        settingsModule,
    )
}
