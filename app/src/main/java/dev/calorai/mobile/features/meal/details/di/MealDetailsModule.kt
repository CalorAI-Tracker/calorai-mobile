package dev.calorai.mobile.features.meal.details.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.meal.details.ui.MealDetailsViewModel
import dev.calorai.mobile.features.meal.domain.usecases.GetGoalParamsForMealUseCase
import dev.calorai.mobile.features.meal.domain.usecases.GetGoalParamsForMealUseCaseImpl
import dev.calorai.mobile.features.meal.domain.usecases.GetMealProgressUseCase
import dev.calorai.mobile.features.meal.domain.usecases.GetMealProgressUseCaseImpl
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val mealDetailsModule = module {
    viewModel {
        MealDetailsViewModel(
            savedStateHandle = get(),
            getMealProgressUseCase = get(),
            mapper = get(),
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>())
        )
    }
    factory<GetMealProgressUseCase> {
        GetMealProgressUseCaseImpl(
            repository = get(),
            getGoalParamsForMealUseCase = get(),
        )
    }
    factory<GetGoalParamsForMealUseCase> {
        GetGoalParamsForMealUseCaseImpl()
    }
}
