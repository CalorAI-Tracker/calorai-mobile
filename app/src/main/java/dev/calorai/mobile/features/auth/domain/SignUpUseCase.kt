package dev.calorai.mobile.features.auth.domain

import dev.calorai.mobile.features.auth.domain.model.SignupPayload

interface SignUpUseCase {

    suspend operator fun invoke(email: String, password: String)
}

class SignUpUseCaseImpl constructor(
    private val authRepository: AuthRepository,
) : SignUpUseCase {

    override suspend fun invoke(email: String, password: String) {
        authRepository.signUp(
            payload = SignupPayload(
                email = email,
                password = password,
            )
        )
    }
}
