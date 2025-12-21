package dev.calorai.mobile.features.main.features.settings.ui

sealed interface SettingsUiEvent {
    data class FormChange(val form: UserSettingsUiState) : SettingsUiEvent
    data object Save : SettingsUiEvent
}
