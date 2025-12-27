package dev.calorai.mobile.features.auth.login.ui

import androidx.credentials.Credential

sealed interface LoginUiEvent {
    data class EmailChanged(val email: String) : LoginUiEvent
    data class PasswordChanged(val password: String) : LoginUiEvent
    data object LoginButtonClick : LoginUiEvent
    data object GoogleLoginClick : LoginUiEvent
    data object RegisterClick : LoginUiEvent
    data class GoogleCredentials(val credential: Credential) : LoginUiEvent
    data class GoogleError(val error: Exception) : LoginUiEvent
}