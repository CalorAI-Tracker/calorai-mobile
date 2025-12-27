package dev.calorai.mobile.features.plan.di

import dev.calorai.mobile.core.network.di.NetworkContext
import dev.calorai.mobile.features.plan.data.api.PlanApi
import dev.calorai.mobile.features.plan.ui.PlanViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module
import retrofit2.Retrofit

internal val planModule = module {

    viewModelOf(::PlanViewModel)

    single { get<Retrofit>(qualifier<NetworkContext.Authorized>()).create(PlanApi::class.java) }
}
