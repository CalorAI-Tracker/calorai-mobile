package dev.calorai.mobile.features.main.features.settings.domain

import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserProfilePayload

interface UpdateUserHealthProfileUseCase {

    suspend operator fun invoke(
        userId: Long,
        payload: UpdateUserProfilePayload,
    )
}

internal class UpdateUserHealthProfileUseCaseImpl(
    private val repository: SettingsRepository,
) : UpdateUserHealthProfileUseCase {

    override suspend fun invoke(
        userId: Long,
        payload: UpdateUserProfilePayload,
    ) {
        repository.updateUserProfile(userId, payload)
    }
}
