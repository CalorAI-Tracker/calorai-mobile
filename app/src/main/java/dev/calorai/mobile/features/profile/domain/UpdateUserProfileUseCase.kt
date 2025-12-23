package dev.calorai.mobile.features.profile.domain

import dev.calorai.mobile.features.profile.domain.model.UpdateUserProfilePayload

interface UpdateUserProfileUseCase {

    suspend operator fun invoke(
        userId: Long,
        payload: UpdateUserProfilePayload,
    )
}

internal class UpdateUserProfileUseCaseImpl(
    private val repository: ProfileRepository,
) : UpdateUserProfileUseCase {

    override suspend fun invoke(
        userId: Long,
        payload: UpdateUserProfilePayload,
    ) {
        repository.updateUserProfile(userId, payload)
    }
}
