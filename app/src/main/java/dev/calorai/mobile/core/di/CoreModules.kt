package dev.calorai.mobile.core.di

import dev.calorai.mobile.core.navigation.di.globalNavigationModule
import dev.calorai.mobile.core.network.di.networkModule
import org.koin.dsl.module

internal val coreModules = module {
    includes(
        networkModule,
        globalNavigationModule,
    )
}
