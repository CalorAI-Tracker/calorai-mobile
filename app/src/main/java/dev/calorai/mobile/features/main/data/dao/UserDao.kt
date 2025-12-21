package dev.calorai.mobile.features.main.data.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.calorai.mobile.features.main.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM user LIMIT 1")
    fun observeUser(): Flow<UserEntity>

    @Update
    suspend fun update(user: UserEntity)

    @Query("DELETE FROM user")
    suspend fun clear()
}