package dev.calorai.mobile.features.main.features.settings.domain

import dev.calorai.mobile.features.main.features.settings.domain.model.UserHealthProfile

interface GetUserHealthProfileUseCase {
    suspend operator fun invoke(userId: Long): UserHealthProfile?
}

internal class GetUserHealthProfileUseCaseImpl(
    private val repository: SettingsRepository,
) : GetUserHealthProfileUseCase {

    override suspend fun invoke(userId: Long): UserHealthProfile? {
        return repository.getUserHealthProfile(userId)
    }
}
