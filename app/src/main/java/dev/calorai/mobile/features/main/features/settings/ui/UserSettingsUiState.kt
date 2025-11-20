package dev.calorai.mobile.features.main.features.settings.ui

data class UserSettingsUiState(
    val name: String = "",
    val email: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val height: String = "",
    val weight: String = "",
    val activity: String = "",
    val goal: String = ""
)