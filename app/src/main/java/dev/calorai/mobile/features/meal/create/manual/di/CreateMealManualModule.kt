package dev.calorai.mobile.features.meal.create.manual.di

import dev.calorai.mobile.features.meal.create.manual.ui.CreateMealManualViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

internal val createMealManualModule = module {
    viewModel {
        CreateMealManualViewModel(
            savedStateHandle = get(),
            createMealEntryUseCase = get()
        )
    }
}
