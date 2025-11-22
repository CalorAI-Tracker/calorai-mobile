package dev.calorai.mobile.core.navigation.di

import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.navigation.RouterControllerImpl
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val globalNavigationModule = module {
    single<RouterController>(qualifier<GlobalRouterContext>()) { RouterControllerImpl() }
}
