package dev.calorai.mobile.features.meal.create.di

import dev.calorai.mobile.features.meal.create.ui.CreateMealViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val createMealModule = module {
    viewModelOf(::CreateMealViewModel)
}
