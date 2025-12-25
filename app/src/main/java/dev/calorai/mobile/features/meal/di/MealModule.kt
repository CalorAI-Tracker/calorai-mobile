package dev.calorai.mobile.features.meal.di

import dev.calorai.mobile.core.network.di.RETROFIT_AUTHORIZED
import dev.calorai.mobile.features.meal.create.manual.di.createMealManualModule
import dev.calorai.mobile.features.meal.data.api.MealApi
import dev.calorai.mobile.features.meal.data.mappers.MealMapper
import dev.calorai.mobile.features.meal.data.repository.MealRepositoryImpl
import dev.calorai.mobile.features.meal.details.di.mealDetailsModule
import dev.calorai.mobile.features.meal.domain.MealRepository
import dev.calorai.mobile.features.meal.domain.usecases.ClearAllMealsUseCase
import dev.calorai.mobile.features.meal.domain.usecases.ClearAllMealsUseCaseImpl
import dev.calorai.mobile.features.meal.domain.usecases.CreateMealEntryUseCase
import dev.calorai.mobile.features.meal.domain.usecases.CreateMealEntryUseCaseImpl
import dev.calorai.mobile.features.meal.domain.usecases.DeleteMealUseCase
import dev.calorai.mobile.features.meal.domain.usecases.DeleteMealUseCaseImpl
import dev.calorai.mobile.features.meal.domain.usecases.DeleteMealsByDateUseCase
import dev.calorai.mobile.features.meal.domain.usecases.DeleteMealsByDateUseCaseImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

internal val mealModule = module {

    single { get<Retrofit>(named(RETROFIT_AUTHORIZED)).create(MealApi::class.java) }
    factory { MealMapper() }
    factory<ClearAllMealsUseCase> {
        ClearAllMealsUseCaseImpl(repository = get())
    }
    factory<CreateMealEntryUseCase> {
        CreateMealEntryUseCaseImpl(repository = get())
    }
    factory<DeleteMealsByDateUseCase> {
        DeleteMealsByDateUseCaseImpl(repository = get())
    }
    factory<DeleteMealUseCase> {
        DeleteMealUseCaseImpl(repository = get())
    }
    single<MealRepository> {
        MealRepositoryImpl(
            api = get(),
            dailyMealsDao = get(),
            userDao = get(),
            mapper = get(),
        )
    }

    includes(
        createMealManualModule,
        mealDetailsModule,
    )
}
