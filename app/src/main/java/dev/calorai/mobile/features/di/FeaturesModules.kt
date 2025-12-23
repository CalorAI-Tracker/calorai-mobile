package dev.calorai.mobile.features.di

import dev.calorai.mobile.features.auth.di.authModule
import dev.calorai.mobile.features.home.di.homeModule
import dev.calorai.mobile.features.main.di.mainModule
import dev.calorai.mobile.features.meal.di.mealModule
import dev.calorai.mobile.features.plan.di.planModule
import dev.calorai.mobile.features.profile.di.profileModule
import dev.calorai.mobile.features.progress.di.progressModule
import dev.calorai.mobile.features.splash.di.splashModule
import org.koin.dsl.module

internal val featureModules = module {

    includes(
        splashModule,
        authModule,
        mainModule,
        homeModule,
        planModule,
        progressModule,
        profileModule,
        mealModule,
    )
}
