package dev.calorai.mobile.features.profile.domain

import dev.calorai.mobile.features.profile.domain.model.CreateUserProfilePayload

interface CreateUserProfileUseCase {

    suspend operator fun invoke(
        payload: CreateUserProfilePayload,
    )
}

internal class CreateUserProfileUseCaseImpl(
    private val repository: ProfileRepository,
) : CreateUserProfileUseCase {

    override suspend fun invoke(
        payload: CreateUserProfilePayload,
    ) {
        repository.createUserProfile(payload)
    }
}
