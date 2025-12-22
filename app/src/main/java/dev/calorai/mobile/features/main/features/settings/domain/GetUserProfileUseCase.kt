package dev.calorai.mobile.features.main.features.settings.domain

import dev.calorai.mobile.features.main.features.settings.domain.model.UserProfile

interface GetUserProfileUseCase {
    suspend operator fun invoke(userId: Long): UserProfile?
}

internal class GetUserProfileUseCaseImpl(
    private val repository: SettingsRepository,
) : GetUserProfileUseCase {

    override suspend fun invoke(userId: Long): UserProfile? {
        return repository.getUserProfile(userId)
    }
}
