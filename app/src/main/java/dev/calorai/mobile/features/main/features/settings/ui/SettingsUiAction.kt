package dev.calorai.mobile.features.main.features.settings.ui

import dev.calorai.mobile.features.main.features.settings.ui.model.SavingErrorType

sealed interface SettingsUiAction {

    data class ShowErrorMessage(
        val errorType: SavingErrorType,
        val value: String? = null,
    ) : SettingsUiAction

    data object ShowSavingMessage : SettingsUiAction
}

