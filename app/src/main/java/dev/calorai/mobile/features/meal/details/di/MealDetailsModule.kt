package dev.calorai.mobile.features.meal.details.di

import dev.calorai.mobile.features.meal.details.ui.MealDetailsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val mealDetailsModule = module {
    viewModelOf(::MealDetailsViewModel)
}
