package dev.calorai.mobile.features.profile.ui

import dev.calorai.mobile.features.profile.ui.model.UserProfileUi

data class ProfileUiState(
    val user: UserProfileUi = UserProfileUi(),
    val isSaving: Boolean = false,
)
