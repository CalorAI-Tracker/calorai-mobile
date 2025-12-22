package dev.calorai.mobile.features.main.features.settings.data

import android.content.Context
import androidx.core.os.ConfigurationCompat
import dev.calorai.mobile.features.main.features.settings.domain.SettingsException
import dev.calorai.mobile.features.main.features.settings.domain.model.Activity
import dev.calorai.mobile.features.main.features.settings.domain.model.Goal
import dev.calorai.mobile.features.main.features.settings.domain.model.Sex
import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserHealthProfilePayload
import dev.calorai.mobile.features.main.features.settings.domain.model.UserHealthProfile
import dev.calorai.mobile.features.main.features.settings.ui.UserSettingsUiState
import dev.calorai.mobile.features.main.features.settings.ui.model.ActivityUi
import dev.calorai.mobile.features.main.features.settings.ui.model.GoalUi
import dev.calorai.mobile.features.main.features.settings.ui.model.SexUi
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

    private val displayDateFormatter: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale)

    private val backendDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    fun mapToDomainPayload(uiState: UserSettingsUiState): UpdateUserHealthProfilePayload {
        return UpdateUserHealthProfilePayload(
            sex = uiState.gender.toSexCode(),
            height = uiState.height.extractNumberOrThrow(),
            weight = uiState.weight.extractNumberOrThrow(),
            birthDay = uiState.birthDate.toBackendDate(),
            activityCode = uiState.activity.toActivityCode(),
            healthGoalCode = uiState.goal.toGoalCode(),
            targetWeightKg = null,
            weeklyRateKg = null,
        )
    }

    fun mapToUiState(profile: UserHealthProfile): UserSettingsUiState {
        return UserSettingsUiState(
            name = "",
            email = "",
            birthDate = profile.birthDay.fromBackendDate(),
            gender = profile.sex.toSexUiString(),
            height = profile.height.toString(),
            weight = profile.weight.toString(),
            activity = profile.activityCode.toActivityUiString(),
            goal = profile.healthGoalCode.toGoalUiString(),
        )
    }

    private fun String.toBackendDate(): String {
        val localDate = LocalDate.parse(this, displayDateFormatter)
        return localDate.format(backendDateFormatter)
    }

    private fun String.extractNumberOrThrow(): Long {
        val digits = this.takeWhile { it.isDigit() }
        return digits.toLongOrNull()
            ?: throw SettingsException.NumberParseError(this)
    }

    private fun String.toSexCode(): String = SexUi.entries.firstOrNull { sex ->
        context.getString(sex.labelResId) == this
    }?.let { sexUi ->
        Sex.entries.firstOrNull { it.name == sexUi.name }?.name
    } ?: throw SettingsException.UnknownGender(this)

    private fun String.toActivityCode(): String = ActivityUi.entries.firstOrNull { activity ->
        context.getString(activity.labelResId) == this
    }?.let { activityUi ->
        Activity.entries.firstOrNull { it.name == activityUi.name }?.name
    } ?: throw SettingsException.UnknownActivity(this)

    private fun String.toGoalCode(): String = GoalUi.entries.firstOrNull { goal ->
        context.getString(goal.labelResId) == this
    }?.let { goalUi ->
        Goal.entries.firstOrNull { it.name == goalUi.name }?.name
    } ?: throw SettingsException.UnknownGoal(this)

    private fun String.fromBackendDate(): String {
        val localDate = LocalDate.parse(this, backendDateFormatter)
        return localDate.format(displayDateFormatter)
    }

    private fun String.toSexUiString(): String {
        val sex = Sex.entries.firstOrNull { it.name == this }
            ?: return ""
        val sexUi = SexUi.entries.firstOrNull { it.name == sex.name }
            ?: return ""
        return context.getString(sexUi.labelResId)
    }

    private fun String.toActivityUiString(): String {
        val activity = Activity.entries.firstOrNull { it.name == this }
            ?: return ""
        val activityUi = ActivityUi.entries.firstOrNull { it.name == activity.name }
            ?: return ""
        return context.getString(activityUi.labelResId)
    }

    private fun String.toGoalUiString(): String {
        val goal = Goal.entries.firstOrNull { it.name == this }
            ?: return ""
        val goalUi = GoalUi.entries.firstOrNull { it.name == goal.name }
            ?: return ""
        return context.getString(goalUi.labelResId)
    }
}
