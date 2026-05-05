package dev.calorai.mobile.features.meal.ready.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.meal.ready.ui.MealReadyListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val mealReadyListModule = module {
    viewModel {
        MealReadyListViewModel(
            savedStateHandle = get(),
            searchFoodCatalogUseCase = get(),
            createMealEntryUseCase = get(),
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
}
