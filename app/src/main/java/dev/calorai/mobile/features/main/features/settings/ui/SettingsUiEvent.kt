package dev.calorai.mobile.features.main.features.settings.ui

import dev.calorai.mobile.features.main.features.settings.ui.model.ActivityUi
import dev.calorai.mobile.features.main.features.settings.ui.model.GenderUi
import dev.calorai.mobile.features.main.features.settings.ui.model.GoalUi

sealed interface SettingsUiEvent {
    data class NameChange(val value: String) : SettingsUiEvent
    data class EmailChange(val value: String) : SettingsUiEvent
    data class BirthDateChange(val selectedDateMillis: Long) : SettingsUiEvent
    data class GenderChange(val value: GenderUi) : SettingsUiEvent
    data class HeightChange(val value: Int) : SettingsUiEvent
    data class WeightChange(val value: Int) : SettingsUiEvent
    data class ActivityChange(val value: ActivityUi) : SettingsUiEvent
    data class GoalChange(val value: GoalUi) : SettingsUiEvent

    data object SaveButtonClick : SettingsUiEvent
}
