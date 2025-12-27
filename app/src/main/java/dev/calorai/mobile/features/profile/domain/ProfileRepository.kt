package dev.calorai.mobile.features.profile.domain

import dev.calorai.mobile.features.profile.domain.model.CreateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.UpdateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.UserProfile

interface ProfileRepository {

    suspend fun getUserProfile(): UserProfile?
    suspend fun updateUserProfile(payload: UpdateUserProfilePayload)
    suspend fun createUserProfile(payload: CreateUserProfilePayload)
}
