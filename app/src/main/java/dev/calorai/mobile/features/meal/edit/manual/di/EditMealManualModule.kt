package dev.calorai.mobile.features.meal.edit.manual.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.meal.edit.manual.ui.MealManualEditorViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val mealManualEditorModule = module {
    viewModel {
        MealManualEditorViewModel(
            savedStateHandle = get(),
            createMealEntryUseCase = get(),
            updateMealEntryUseCase = get(),
            getMealEntryUseCase = get(),
            recognizeMealUseCase = get(),
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
}
