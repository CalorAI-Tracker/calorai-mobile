package dev.calorai.mobile.core.network.di

sealed interface NetworkContext {
    data object Base : NetworkContext
    data object Authorized : NetworkContext
}
