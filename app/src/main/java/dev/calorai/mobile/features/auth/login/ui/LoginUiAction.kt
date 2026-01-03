package dev.calorai.mobile.features.auth.login.ui

sealed interface LoginUiAction {
    data object GoogleAuth : LoginUiAction
    data object ShowErrorMessage : LoginUiAction
}
