package dev.calorai.mobile.features.auth.domain

interface LogoutUseCase {

    suspend operator fun invoke()
}

class LogoutUseCaseImpl constructor(
    private val authRepository: AuthRepository,
) : LogoutUseCase {

    override suspend fun invoke() {
        authRepository.logout()
    }
}
