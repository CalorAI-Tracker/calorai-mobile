package dev.calorai.mobile.features.main.features.settings.domain

import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserProfilePayload
import dev.calorai.mobile.features.main.features.settings.domain.model.UserProfile

interface SettingsRepository {

    suspend fun getUserProfile(userId: Long): UserProfile?
    suspend fun updateUserProfile(
        userId: Long,
        payload: UpdateUserProfilePayload,
    )
}
