package dev.calorai.mobile.features.profile.domain

import android.content.Context
import dev.calorai.mobile.features.profile.domain.model.UserId

interface UserIdStore {

    suspend fun getUserId(): UserId?
    suspend fun setUserId(userId: UserId)
    suspend fun clearUserId()
}

class UserIdStoreImpl constructor(
    private val context: Context,
) : UserIdStore {

    private companion object {
        private const val USER_ID_PREFS = "user_id_prefs"
        private const val USER_ID_KEY = "user_id"
        private const val UNDEFINED_USER_ID = -1L
    }

    private val prefs by lazy {
        context.getSharedPreferences(USER_ID_PREFS, Context.MODE_PRIVATE)
    }

    override suspend fun getUserId(): UserId? {
        val userId = prefs.getLong(USER_ID_KEY, UNDEFINED_USER_ID)
        return userId.takeUnless { userId == UNDEFINED_USER_ID }?.let { UserId(it) }
    }

    override suspend fun setUserId(userId: UserId) {
        prefs.edit().apply {
            putLong(USER_ID_KEY, userId.value)
            commit()
        }
    }

    override suspend fun clearUserId() {
        prefs.edit().apply {
            putLong(USER_ID_KEY, UNDEFINED_USER_ID)
            commit()
        }
    }
}
