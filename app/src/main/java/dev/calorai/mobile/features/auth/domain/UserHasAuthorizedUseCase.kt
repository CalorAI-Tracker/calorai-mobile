package dev.calorai.mobile.features.auth.domain

import dev.calorai.mobile.features.auth.data.token.tokenProvider.TokenProvider

interface UserHasAuthorizedUseCase {
    operator fun invoke(): Boolean
}

class UserHasAuthorizedUseCaseImpl(
    private val tokenProvider: TokenProvider
) : UserHasAuthorizedUseCase {

    override fun invoke(): Boolean {
        return tokenProvider.getAccessToken().isNullOrEmpty().not()
    }
}