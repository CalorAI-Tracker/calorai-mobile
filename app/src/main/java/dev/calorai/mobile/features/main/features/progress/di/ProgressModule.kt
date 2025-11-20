package dev.calorai.mobile.features.main.features.progress.di

import dev.calorai.mobile.features.main.features.progress.ui.ProgressViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val progressModule = module {
    viewModelOf(::ProgressViewModel)
}
