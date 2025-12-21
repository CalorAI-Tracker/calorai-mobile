package dev.calorai.mobile.features.main.features.settings.ui

data class SettingsViewState(
    val form: UserSettingsUiState = UserSettingsUiState(),
    val isSaving: Boolean = false,
)
