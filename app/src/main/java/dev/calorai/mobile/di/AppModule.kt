package dev.calorai.mobile.di

import dev.calorai.mobile.auth.di.authModule
import dev.calorai.mobile.main.di.mainModule
import org.koin.dsl.module

internal val appModule = module {
    includes(
        authModule,
        mainModule,
    )
}
