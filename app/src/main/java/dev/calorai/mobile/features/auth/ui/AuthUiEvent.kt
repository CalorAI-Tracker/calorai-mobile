package dev.calorai.mobile.features.auth.ui

sealed interface AuthUiEvent {
    data class EmailChanged(val email: String) : AuthUiEvent
    data class PasswordChanged(val password: String) : AuthUiEvent
    data object LoginButtonClick : AuthUiEvent
    data object GoogleLoginClick : AuthUiEvent
    data object RegisterClick : AuthUiEvent
}