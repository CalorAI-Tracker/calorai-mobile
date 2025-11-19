package dev.calorai.mobile.main.features.home.ui

import dev.calorai.mobile.main.features.home.ui.model.DateUiModel

sealed interface HomeUiEvent {
    data class SelectDate(val date: DateUiModel) : HomeUiEvent
}
