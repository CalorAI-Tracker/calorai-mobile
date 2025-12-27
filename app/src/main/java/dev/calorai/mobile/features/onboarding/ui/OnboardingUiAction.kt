package dev.calorai.mobile.features.onboarding.ui

sealed interface OnboardingUiAction {

    data class ScrollToPage(val page: Int) : OnboardingUiAction
}
