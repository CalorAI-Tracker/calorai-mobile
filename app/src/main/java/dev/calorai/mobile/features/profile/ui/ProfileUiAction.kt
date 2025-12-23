package dev.calorai.mobile.features.profile.ui

import dev.calorai.mobile.features.profile.ui.model.SavingErrorType

sealed interface ProfileUiAction {

    data object ShowNetworkError : ProfileUiAction

    data class ShowErrorMessage(
        val errorType: SavingErrorType,
        val value: String? = null,
    ) : ProfileUiAction

    data object ShowSavingMessage : ProfileUiAction
}

