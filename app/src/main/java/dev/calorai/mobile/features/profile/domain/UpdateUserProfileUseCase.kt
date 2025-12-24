package dev.calorai.mobile.features.profile.domain

import dev.calorai.mobile.features.profile.domain.model.UpdateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.UserId

interface UpdateUserProfileUseCase {

    suspend operator fun invoke(
        userId: UserId,
        payload: UpdateUserProfilePayload,
    )
}

internal class UpdateUserProfileUseCaseImpl(
    private val repository: ProfileRepository,
) : UpdateUserProfileUseCase {

    override suspend fun invoke(
        userId: UserId,
        payload: UpdateUserProfilePayload,
    ) {
        repository.updateUserProfile(userId, payload)
    }
}
