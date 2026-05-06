package dev.calorai.mobile.features.meal.ready.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.meal.ready.ui.MealReadyListViewModel
import dev.calorai.mobile.features.meal.ready.ui.MealReadyUiMapper
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val mealReadyListModule = module {
    factory {
        MealReadyUiMapper()
    }

    viewModel {
        MealReadyListViewModel(
            savedStateHandle = get(),
            context = androidContext(),
            searchFoodCatalogUseCase = get(),
            createMealEntryUseCase = get(),
            uiMapper = get(),
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
}
