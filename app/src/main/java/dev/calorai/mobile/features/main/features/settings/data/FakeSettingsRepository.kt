package dev.calorai.mobile.features.main.features.settings.data

import dev.calorai.mobile.features.main.features.settings.domain.SettingsRepository
import dev.calorai.mobile.features.main.features.settings.domain.model.UpdateUserHealthProfilePayload
import kotlinx.coroutines.delay

/**
 * Временная реализация, чтобы бизнес-логика сохранения работала без реального бэка.
 * Заменить на сетевую реализацию
 */
class FakeSettingsRepository : SettingsRepository {

    override suspend fun updateUserHealthProfile(
        userId: Long,
        payload: UpdateUserHealthProfilePayload,
    ) {
        // Имитация сетевой задержки и успешного ответа
        delay(300)
    }
}



