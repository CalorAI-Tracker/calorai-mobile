package dev.calorai.mobile.features.auth.di

import dev.calorai.mobile.features.auth.ui.AuthViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val authModule = module {
    viewModelOf(::AuthViewModel)
}
