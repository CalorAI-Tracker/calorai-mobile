package dev.calorai.mobile.main.features.plan.di

import dev.calorai.mobile.main.features.plan.ui.PlanViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val planModule = module {
    viewModelOf(::PlanViewModel)
}
