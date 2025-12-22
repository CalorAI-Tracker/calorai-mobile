package dev.calorai.mobile.features.main.features.settings.ui

import dev.calorai.mobile.features.main.features.settings.ui.model.UserProfileUi

data class SettingsUiState(
    val user: UserProfileUi = UserProfileUi(),
    val isSaving: Boolean = false,
)
