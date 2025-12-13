package dev.calorai.mobile.features.meal.create.manual.di

import dev.calorai.mobile.features.meal.create.manual.ui.CreateMealManualViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val createMealManualModule = module {
    viewModelOf(::CreateMealManualViewModel)
}
