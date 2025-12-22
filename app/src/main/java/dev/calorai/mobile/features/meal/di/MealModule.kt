package dev.calorai.mobile.features.meal.di

import dev.calorai.mobile.core.network.di.RETROFIT_AUTHORIZED
import dev.calorai.mobile.features.meal.create.manual.di.createMealManualModule
import dev.calorai.mobile.features.meal.data.api.MealApi
import dev.calorai.mobile.features.meal.details.di.mealDetailsModule
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

internal val mealModule = module {

    single { get<Retrofit>(named(RETROFIT_AUTHORIZED)).create(MealApi::class.java) }

    includes(
        createMealManualModule,
        mealDetailsModule,
    )
}
