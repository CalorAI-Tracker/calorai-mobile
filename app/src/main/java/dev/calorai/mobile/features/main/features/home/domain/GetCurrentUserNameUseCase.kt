package dev.calorai.mobile.features.main.features.home.domain

import dev.calorai.mobile.features.main.data.dao.UserDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

interface GetCurrentUserNameUseCase {
    operator fun invoke(): Flow<String>
}

internal class GetCurrentUserNameUseCaseImpl constructor(
    private val userDao: UserDao,
) : GetCurrentUserNameUseCase {

    override fun invoke(): Flow<String> {
        return userDao.observeUser().map { it.name }.catch { emit("") }
    }
}
