package dev.calorai.mobile.features.profile.domain

import dev.calorai.mobile.features.profile.domain.model.CreateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.UserId

interface CreateUserProfileUseCase {

    suspend operator fun invoke(
        payload: CreateUserProfilePayload
    ) : UserId
}

internal class CreateUserProfileUseCaseImpl(
    private val repository: ProfileRepository,
) : CreateUserProfileUseCase {

    override suspend fun invoke(
        payload: CreateUserProfilePayload
    ) : UserId {
        return repository.createUserProfile(payload)
    }
}
