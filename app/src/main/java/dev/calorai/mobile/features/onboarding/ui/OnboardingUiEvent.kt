package dev.calorai.mobile.features.onboarding.ui

import dev.calorai.mobile.features.profile.ui.model.ActivityUi
import dev.calorai.mobile.features.profile.ui.model.GenderUi
import dev.calorai.mobile.features.profile.ui.model.GoalUi

sealed interface OnboardingUiEvent {
    data class BirthDateChange(val selectedDateMillis: Long) : OnboardingUiEvent
    data class GenderChange(val value: GenderUi) : OnboardingUiEvent
    data class HeightChange(val value: Int) : OnboardingUiEvent
    data class WeightChange(val value: Int) : OnboardingUiEvent
    data class ActivityChange(val value: ActivityUi) : OnboardingUiEvent
    data class GoalChange(val value: GoalUi) : OnboardingUiEvent

    data object BackButtonClick : OnboardingUiEvent
    data object NextButtonClick : OnboardingUiEvent
}