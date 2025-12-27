package dev.calorai.mobile.features.auth.domain

import dev.calorai.mobile.core.local.deviceId.DeviceIdStore
import dev.calorai.mobile.features.auth.domain.model.LoginPayload

interface LoginUseCase {

    suspend operator fun invoke(email: String, password: String)
}

class LoginUseCaseImpl constructor(
    private val authRepository: AuthRepository,
    private val deviceIdStore: DeviceIdStore,
) : LoginUseCase {

    override suspend fun invoke(email: String, password: String) {
        authRepository.login(
            payload = LoginPayload(
                email = email,
                password = password,
                deviceId = deviceIdStore.getDeviceId(),
            )
        )
    }
}
