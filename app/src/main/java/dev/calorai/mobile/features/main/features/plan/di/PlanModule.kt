package dev.calorai.mobile.features.main.features.plan.di

import dev.calorai.mobile.features.main.features.plan.ui.PlanViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val planModule = module {
    viewModelOf(::PlanViewModel)
}
