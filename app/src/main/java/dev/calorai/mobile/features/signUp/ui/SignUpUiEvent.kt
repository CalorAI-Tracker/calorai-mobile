package dev.calorai.mobile.features.signUp.ui

sealed interface SignUpUiEvent {
    data class NameEntered(val name: String) : SignUpUiEvent
    data class EmailEntered(val email: String) : SignUpUiEvent
    data class PasswordEntered(val password: String) : SignUpUiEvent


    data object SignUpButtonClick : SignUpUiEvent
    data object LoginClick : SignUpUiEvent
}