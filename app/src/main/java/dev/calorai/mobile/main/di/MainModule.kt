package dev.calorai.mobile.main.di

import dev.calorai.mobile.main.features.home.di.homeModule
import dev.calorai.mobile.main.features.plan.di.planModule
import dev.calorai.mobile.main.features.progress.di.progressModule
import dev.calorai.mobile.main.features.settings.di.settingsModule
import dev.calorai.mobile.main.ui.MainViewModel
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
