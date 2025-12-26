package dev.calorai.mobile.features.profile.ui

import dev.calorai.mobile.features.profile.ui.model.ActivityUi
import dev.calorai.mobile.features.profile.ui.model.GenderUi
import dev.calorai.mobile.features.profile.ui.model.GoalUi

sealed interface ProfileUiEvent {
    data class NameChange(val value: String) : ProfileUiEvent
    data class EmailChange(val value: String) : ProfileUiEvent
    data class BirthDateChange(val selectedDateMillis: Long) : ProfileUiEvent
    data class GenderChange(val value: GenderUi) : ProfileUiEvent
    data class HeightChange(val value: Int) : ProfileUiEvent
    data class WeightChange(val value: Int) : ProfileUiEvent
    data class ActivityChange(val value: ActivityUi) : ProfileUiEvent
    data class GoalChange(val value: GoalUi) : ProfileUiEvent

    data object SaveButtonClick : ProfileUiEvent
    data object OnRefresh : ProfileUiEvent
    data object LogoutClick : ProfileUiEvent
}
