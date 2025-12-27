package dev.calorai.mobile.features.onboarding.data

import android.content.Context
import dev.calorai.mobile.core.utils.locale
import dev.calorai.mobile.features.onboarding.ui.OnboardingUiState
import dev.calorai.mobile.features.profile.domain.error.ProfileException
import dev.calorai.mobile.features.profile.domain.model.Activity
import dev.calorai.mobile.features.profile.domain.model.CreateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.Gender
import dev.calorai.mobile.features.profile.domain.model.Goal
import dev.calorai.mobile.features.profile.ui.model.ActivityUi
import dev.calorai.mobile.features.profile.ui.model.GenderUi
import dev.calorai.mobile.features.profile.ui.model.GoalUi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OnboardingMapper(context: Context) {

    private val uiDateFormatter by lazy {
        DateTimeFormatter.ofPattern("dd MMMM yyyy", context.locale)
    }

    private val domainDateFormatter by lazy {
        DateTimeFormatter.ofPattern("dd.MM.yyyy", context.locale)
    }

    fun mapLocalDateToUi(localDate: LocalDate): String = localDate.format(uiDateFormatter)

    fun mapBirthdayToDomain(date: String): String {
        val localDate = LocalDate.parse(date, uiDateFormatter)
        return localDate.format(domainDateFormatter)
    }

    fun mapToDomain(onboardingUiState: OnboardingUiState, name: String): CreateUserProfilePayload =
        CreateUserProfilePayload(
            gender = mapToDomain(onboardingUiState.genderPage.gender),
            height = onboardingUiState.heightWeightPage.height.extractNumberOrThrow(),
            weight = onboardingUiState.heightWeightPage.weight.extractNumberOrThrow(),
            birthDay = mapBirthdayToDomain(onboardingUiState.birthdayPage.birthDate),
            name = name,
            activityCode = mapToDomain(onboardingUiState.activityPage.activity),
            healthGoalCode = mapToDomain(onboardingUiState.goalPage.goal),
        )

    private fun mapToDomain(activity: ActivityUi?): Activity = when (activity) {
        ActivityUi.SEDENTARY -> Activity.SEDENTARY
        ActivityUi.LIGHT -> Activity.LIGHT
        ActivityUi.MODERATE -> Activity.MODERATE
        ActivityUi.ACTIVE -> Activity.ACTIVE
        ActivityUi.VERY_ACTIVE -> Activity.VERY_ACTIVE
        else -> throw ProfileException.UnknownActivity(activity.toString())
    }

    private fun mapToDomain(sex: GenderUi?): Gender = when (sex) {
        GenderUi.FEMALE -> Gender.FEMALE
        GenderUi.MALE -> Gender.MALE
        else -> throw ProfileException.UnknownGender(sex.toString())
    }

    private fun mapToDomain(goal: GoalUi?): Goal = when (goal) {
        GoalUi.LOSE_WEIGHT -> Goal.LOSE_WEIGHT
        GoalUi.MAINTAIN -> Goal.MAINTAIN
        GoalUi.GAIN_WEIGHT -> Goal.GAIN_WEIGHT
        GoalUi.GAIN_MUSCLE -> Goal.GAIN_MUSCLE
        else -> throw ProfileException.UnknownGoal(goal.toString())
    }

    private fun Int?.extractNumberOrThrow(): Int {
        return this ?: throw ProfileException.NumberParseError()
    }
}
