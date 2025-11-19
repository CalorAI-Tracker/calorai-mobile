package dev.calorai.mobile.auth.di

import dev.calorai.mobile.auth.ui.AuthViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val authModule = module {
    viewModelOf(::AuthViewModel)
}
