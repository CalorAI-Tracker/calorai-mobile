package dev.calorai.mobile.features.meal.details.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.home.domain.usecases.GetDayProgressUseCase
import dev.calorai.mobile.features.home.domain.usecases.GetDayProgressUseCaseImpl
import dev.calorai.mobile.features.meal.details.ui.MealDetailsViewModel
import dev.calorai.mobile.features.meal.domain.usecases.GetMealIngredientsUseCase
import dev.calorai.mobile.features.meal.domain.usecases.GetMealIngredientsUseCaseImpl
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val mealDetailsModule = module {
    viewModel {
        MealDetailsViewModel(
            savedStateHandle = get(),
            getMealIngredientsUseCase = get(),
            mapper = get(),
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>())
        )
    }
    factory<GetMealIngredientsUseCase> {
        GetMealIngredientsUseCaseImpl(repository = get())
    }
}
