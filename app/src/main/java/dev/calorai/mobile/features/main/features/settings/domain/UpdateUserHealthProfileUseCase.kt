package dev.calorai.mobile.features.main.features.settings.domain

import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserHealthProfilePayload

interface UpdateUserHealthProfileUseCase {
    suspend operator fun invoke(
        userId: Long,
        payload: UpdateUserHealthProfilePayload,
    )
}

internal class UpdateUserHealthProfileUseCaseImpl(
    private val repository: SettingsRepository,
) : UpdateUserHealthProfileUseCase {

    override suspend fun invoke(
        userId: Long,
        payload: UpdateUserHealthProfilePayload,
    ) {
        repository.updateUserHealthProfile(userId, payload)
    }
}
