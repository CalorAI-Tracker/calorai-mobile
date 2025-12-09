package dev.calorai.mobile.features.main.features.settings.domain

import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserHealthProfilePayload

interface SettingsRepository {
    suspend fun updateUserHealthProfile(
        userId: Long,
        payload: UpdateUserHealthProfilePayload,
    )
}



