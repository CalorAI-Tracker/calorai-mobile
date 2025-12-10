package dev.calorai.mobile.features.di

import dev.calorai.mobile.features.auth.di.authModule
import dev.calorai.mobile.features.signUp.di.signUpModule
import dev.calorai.mobile.features.main.di.mainModule
import dev.calorai.mobile.features.meal.create.di.createMealManualModule
import dev.calorai.mobile.features.meal.details.di.mealDetailsModule
import org.koin.dsl.module

internal val featureModules = module {
    includes(
        authModule,
        signUpModule,
        mainModule,
        createMealManualModule,
        mealDetailsModule,
    )
}
