package dev.calorai.mobile.features.meal.di

import dev.calorai.mobile.features.meal.data.api.MealApi
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

internal val mealModule = module {

    single { get<Retrofit>(named("retrofitAuthorized")).create(MealApi::class.java) }

}
