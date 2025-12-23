package dev.calorai.mobile.features.profile.domain

import dev.calorai.mobile.features.profile.domain.model.UpdateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.UserProfile

interface ProfileRepository {

    suspend fun getUserProfile(userId: Long): UserProfile?
    suspend fun updateUserProfile(
        userId: Long,
        payload: UpdateUserProfilePayload,
    )
}
