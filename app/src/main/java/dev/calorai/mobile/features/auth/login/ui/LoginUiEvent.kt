package dev.calorai.mobile.features.auth.login.ui

sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object LoginButtonClick : LoginUiEvent
    data object GoogleLoginClick : LoginUiEvent
    data object RegisterClick : LoginUiEvent
}