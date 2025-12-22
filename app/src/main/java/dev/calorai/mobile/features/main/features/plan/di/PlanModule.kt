package dev.calorai.mobile.features.main.features.plan.di

import dev.calorai.mobile.core.network.di.RETROFIT_AUTHORIZED
import dev.calorai.mobile.features.main.features.plan.data.api.PlanApi
import dev.calorai.mobile.features.main.features.plan.ui.PlanViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

internal val planModule = module {

    viewModelOf(::PlanViewModel)

    single { get<Retrofit>(named(RETROFIT_AUTHORIZED)).create(PlanApi::class.java) }
}
