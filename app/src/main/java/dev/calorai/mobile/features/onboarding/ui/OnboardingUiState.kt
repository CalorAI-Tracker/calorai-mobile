package dev.calorai.mobile.features.onboarding.ui

import dev.calorai.mobile.features.profile.ui.model.ActivityUi
import dev.calorai.mobile.features.profile.ui.model.GenderUi
import dev.calorai.mobile.features.profile.ui.model.GoalUi

data class OnboardingUiState(
    val genderPage: OnboardingPage.GenderPage = OnboardingPage.GenderPage(),
    val heightWeightPage: OnboardingPage.HeightWeightPage = OnboardingPage.HeightWeightPage(),
    val birthdayPage: OnboardingPage.BirthdayPage = OnboardingPage.BirthdayPage(),
    val activityPage: OnboardingPage.ActivityPage = OnboardingPage.ActivityPage(),
    val goalPage: OnboardingPage.GoalPage = OnboardingPage.GoalPage(),
)

sealed interface OnboardingPage {
    companion object {
        const val NUM_PAGES = 5
    }

    data class GenderPage(
        val gender: GenderUi? = null,
    ) : OnboardingPage {
        companion object {
            const val INDEX: Int = 0
        }
    }

    data class HeightWeightPage(
        val height: Int? = null,
        val weight: Int? = null,
    ) : OnboardingPage {
        companion object {
            const val INDEX: Int = 1
        }
    }

    data class BirthdayPage(
        val birthDate: String = "",
    ) : OnboardingPage {
        companion object {
            const val INDEX: Int = 2
        }
    }

    data class ActivityPage(
        val activity: ActivityUi? = null,
    ) : OnboardingPage {
        companion object {
            const val INDEX: Int = 3
        }
    }

    data class GoalPage(
        val goal: GoalUi? = null,
    ) : OnboardingPage {
        companion object {
            const val INDEX: Int = 4
        }
    }
}
