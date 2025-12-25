package dev.calorai.mobile.features.auth.domain

import dev.calorai.mobile.features.profile.domain.UserIdStore

interface UserHasAuthorizedUseCase {
    suspend operator fun invoke(): Boolean
}

class UserHasAuthorizedUseCaseImpl(
    private val userIdStore: UserIdStore,
) : UserHasAuthorizedUseCase {

    override suspend fun invoke(): Boolean {
        return userIdStore.getUserId() != null
    }
}