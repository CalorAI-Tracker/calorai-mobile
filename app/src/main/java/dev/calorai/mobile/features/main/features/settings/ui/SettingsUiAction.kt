package dev.calorai.mobile.features.main.features.settings.ui

sealed interface SettingsUiAction {
    data class ShowErrorMessage(val errorType: SavingErrorType, val value: String? = null) : SettingsUiAction
    data object ShowSavingMessage : SettingsUiAction
}

enum class SavingErrorType {
    NUMBER_PARSE,
    UNKNOWN_GENDER,
    UNKNOWN_ACTIVITY,
    UNKNOWN_GOAL,
    SAVE_ERROR,
}
