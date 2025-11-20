package dev.calorai.mobile.features.main.features.home.ui

import dev.calorai.mobile.core.uikit.weekBar.DateUiModel

sealed interface HomeUiEvent {
    data class SelectDate(val date: DateUiModel) : HomeUiEvent
}
