package dev.calorai.mobile.features.profile.domain

import dev.calorai.mobile.features.profile.domain.model.UserProfile

interface GetUserProfileUseCase {
    suspend operator fun invoke(): UserProfile?
}

internal class GetUserProfileUseCaseImpl(
    private val repository: ProfileRepository,
) : GetUserProfileUseCase {

    override suspend fun invoke(): UserProfile? {
        return repository.getUserProfile()
    }
}
