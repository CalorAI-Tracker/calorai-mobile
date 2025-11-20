package dev.calorai.mobile.di

import dev.calorai.mobile.core.di.coreModules
import dev.calorai.mobile.features.di.featureModules
import org.koin.dsl.module

internal val appModule = module {
    includes(
        coreModules,
        featureModules,
    )
}
