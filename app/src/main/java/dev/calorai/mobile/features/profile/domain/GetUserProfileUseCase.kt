package dev.calorai.mobile.features.profile.domain

import dev.calorai.mobile.features.profile.domain.model.UserProfile

interface GetUserProfileUseCase {
    suspend operator fun invoke(userId: Long): UserProfile?
}

internal class GetUserProfileUseCaseImpl(
    private val repository: ProfileRepository,
) : GetUserProfileUseCase {

    override suspend fun invoke(userId: Long): UserProfile? {
        return repository.getUserProfile(userId)
    }
}
