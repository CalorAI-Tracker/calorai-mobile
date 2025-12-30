package dev.calorai.mobile.features.meal.create.manual.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.features.meal.create.manual.ui.CreateMealManualViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

internal val createMealManualModule = module {
    viewModel {
        CreateMealManualViewModel(
            savedStateHandle = get(),
            createMealEntryUseCase = get(),
            globalRouter = get<RouterController>(qualifier<GlobalRouterContext>()),
        )
    }
}
