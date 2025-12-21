package dev.calorai.mobile.features.main.features.settings.domain

import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserHealthProfilePayload
import dev.calorai.mobile.features.main.features.settings.domain.model.UserHealthProfile

interface SettingsRepository {
    suspend fun getUserHealthProfile(userId: Long): UserHealthProfile?
    suspend fun updateUserHealthProfile(
        userId: Long,
        payload: UpdateUserHealthProfilePayload,
    )
}
