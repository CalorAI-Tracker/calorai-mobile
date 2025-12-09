package dev.calorai.mobile.features.main.features.settings.domain

import android.content.Context
import dev.calorai.mobile.features.main.features.settings.domain.model.Activity
import dev.calorai.mobile.features.main.features.settings.domain.model.Goal
import dev.calorai.mobile.features.main.features.settings.domain.model.Sex
import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserHealthProfilePayload
import dev.calorai.mobile.features.main.features.settings.ui.UserSettingsUiState
import dev.calorai.mobile.R
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class UpdateUserHealthProfileUseCase(
    private val repository: SettingsRepository,
    private val context: Context,
) {

    private val displayDateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
    private val backendDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    suspend operator fun invoke(
        userId: Long,
        uiState: UserSettingsUiState,
    ) {
        val payload = uiState.toPayload()
        repository.updateUserHealthProfile(userId, payload)
    }

    private fun UserSettingsUiState.toPayload(): UpdateUserHealthProfilePayload {
        return UpdateUserHealthProfilePayload(
            sex = gender.toSexCode(),
            height = height.extractNumberOrThrow(),
            weight = weight.extractNumberOrThrow(),
            birthDay = birthDate.toBackendDate(),
            activityCode = activity.toActivityCode(),
            healthGoalCode = goal.toGoalCode(),
            targetWeightKg = null,
            weeklyRateKg = null,
        )
    }

    private fun String.toBackendDate(): String {
        val localDate = LocalDate.parse(this, displayDateFormatter)
        return localDate.format(backendDateFormatter)
    }

    private fun String.extractNumberOrThrow(): Long {
        val digits = this.takeWhile { it.isDigit() }
        return digits.toLongOrNull()
            ?: throw IllegalArgumentException(
                context.getString(R.string.settings_number_parse_error, this)
            )
    }

    private fun String.toSexCode(): String = Sex.entries.firstOrNull { sex ->
        context.getString(sex.labelResId) == this
    }?.name
        ?: throw IllegalArgumentException(
            context.getString(R.string.settings_unknown_gender, this)
        )

    private fun String.toActivityCode(): String = Activity.entries.firstOrNull { activity ->
        context.getString(activity.labelResId) == this
    }?.name
        ?: throw IllegalArgumentException(
            context.getString(R.string.settings_unknown_activity, this)
        )

    private fun String.toGoalCode(): String = Goal.entries.firstOrNull { goal ->
        context.getString(goal.labelResId) == this
    }?.name
        ?: throw IllegalArgumentException(
            context.getString(R.string.settings_unknown_goal, this)
        )
}


