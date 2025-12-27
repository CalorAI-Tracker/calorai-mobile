package dev.calorai.mobile.features.meal.di

import dev.calorai.mobile.core.network.di.NetworkContext
import dev.calorai.mobile.features.meal.create.manual.di.createMealManualModule
import dev.calorai.mobile.features.meal.data.api.MealApi
import dev.calorai.mobile.features.meal.data.mappers.MealMapper
import dev.calorai.mobile.features.meal.data.repository.MealRepositoryImpl
import dev.calorai.mobile.features.meal.details.di.mealDetailsModule
import dev.calorai.mobile.features.meal.domain.MealRepository
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

internal val mealModule = module {

    single { get<Retrofit>(qualifier<NetworkContext.Authorized>()).create(MealApi::class.java) }

    factory { MealMapper() }

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
