package dev.calorai.mobile.features.auth.ui

sealed interface AuthUiEvent {
    data object ButtonClick : AuthUiEvent
}