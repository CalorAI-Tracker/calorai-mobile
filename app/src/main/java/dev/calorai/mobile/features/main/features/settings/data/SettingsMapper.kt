package dev.calorai.mobile.features.main.features.settings.data

import android.content.Context
import androidx.core.os.ConfigurationCompat
import dev.calorai.mobile.features.main.features.settings.domain.SettingsException
import dev.calorai.mobile.features.main.features.settings.domain.model.Activity
import dev.calorai.mobile.features.main.features.settings.domain.model.Goal
import dev.calorai.mobile.features.main.features.settings.domain.model.Gender
import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserProfilePayload
import dev.calorai.mobile.features.main.features.settings.domain.model.UserProfile
import dev.calorai.mobile.features.main.features.settings.ui.model.ActivityUi
import dev.calorai.mobile.features.main.features.settings.ui.model.GoalUi
import dev.calorai.mobile.features.main.features.settings.ui.model.GenderUi
import dev.calorai.mobile.features.main.features.settings.ui.model.UserProfileUi
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class SettingsMapper(
    private val context: Context,
) {
    private val locale: Locale
        get() {
            val locales = ConfigurationCompat.getLocales(context.resources.configuration)
            return locales[0] ?: Locale.getDefault()
        }

    private val uiDateFormatter by lazy {
        DateTimeFormatter.ofPattern("dd MMMM yyyy", locale)
    }

    private val domainDateFormatter by lazy {
        DateTimeFormatter.ofPattern("dd.MM.yyyy", locale)
    }

    private fun mapBirthdayToUi(date: String): String {
        val localDate = LocalDate.parse(date, domainDateFormatter)
        return localDate.format(uiDateFormatter)
    }

    fun mapLocalDateToUi(localDate: LocalDate): String = localDate.format(uiDateFormatter)

    fun mapBirthdayToDomain(date: String): String {
        val localDate = LocalDate.parse(date, uiDateFormatter)
        return localDate.format(domainDateFormatter)
    }

    fun mapToUi(user: UserProfile): UserProfileUi = UserProfileUi(
        name = user.name,
        email = user.email,
        birthDate = mapBirthdayToUi(user.birthDay),
        gender = mapToUi(user.gender),
        height = user.height,
        weight = user.weight,
        activity = mapToUi(user.activityCode),
        goal = mapToUi(user.healthGoalCode),
    )

    fun mapToDomainPayload(uiState: UserProfileUi): UpdateUserProfilePayload =
        UpdateUserProfilePayload(
            name = uiState.name.ifEmpty { throw SettingsException.EmptyField() },
            email = uiState.email.ifEmpty { throw SettingsException.EmptyField() },
            gender = mapToDomain(uiState.gender),
            height = uiState.height.extractNumberOrThrow(),
            weight = uiState.weight.extractNumberOrThrow(),
            birthDay = mapBirthdayToDomain(
                date = uiState.birthDate.ifEmpty { throw SettingsException.BirthDateParseError() },
            ),
            activityCode = mapToDomain(uiState.activity),
            healthGoalCode = mapToDomain(uiState.goal),
            targetWeightKg = null,
            weeklyRateKg = null,
        )

    private fun mapToUi(activity: Activity): ActivityUi = when (activity) {
        Activity.LIGHT -> ActivityUi.LIGHT
        Activity.MODERATE -> ActivityUi.MODERATE
        Activity.ACTIVE -> ActivityUi.ACTIVE
        Activity.VERY_ACTIVE -> ActivityUi.VERY_ACTIVE
    }

    private fun mapToUi(gender: Gender): GenderUi = when (gender) {
        Gender.FEMALE -> GenderUi.FEMALE
        Gender.MALE -> GenderUi.MALE
    }

    private fun mapToUi(goal: Goal): GoalUi = when (goal) {
        Goal.LOSE_WEIGHT -> GoalUi.LOSE_WEIGHT
        Goal.KEEP_WEIGHT -> GoalUi.KEEP_WEIGHT
        Goal.GAIN_WEIGHT -> GoalUi.GAIN_WEIGHT
    }

    private fun mapToDomain(activity: ActivityUi?): Activity = when (activity) {
        ActivityUi.LIGHT -> Activity.LIGHT
        ActivityUi.MODERATE -> Activity.MODERATE
        ActivityUi.ACTIVE -> Activity.ACTIVE
        ActivityUi.VERY_ACTIVE -> Activity.VERY_ACTIVE
        else -> throw SettingsException.UnknownActivity(activity.toString())
    }

    private fun mapToDomain(sex: GenderUi?): Gender = when (sex) {
        GenderUi.FEMALE -> Gender.FEMALE
        GenderUi.MALE -> Gender.MALE
        else -> throw SettingsException.UnknownGender(sex.toString())
    }

    private fun mapToDomain(goal: GoalUi?): Goal = when (goal) {
        GoalUi.LOSE_WEIGHT -> Goal.LOSE_WEIGHT
        GoalUi.KEEP_WEIGHT -> Goal.KEEP_WEIGHT
        GoalUi.GAIN_WEIGHT -> Goal.GAIN_WEIGHT
        else -> throw SettingsException.UnknownGoal(goal.toString())
    }

    private fun Int?.extractNumberOrThrow(): Int {
        return this ?: throw SettingsException.NumberParseError()
    }
}
