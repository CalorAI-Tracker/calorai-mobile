package dev.calorai.mobile.features.profile.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import dev.calorai.mobile.features.profile.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity)

    @Query("SELECT * FROM ${UserEntity.TABLE} LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Query("SELECT user_id FROM ${UserEntity.TABLE} LIMIT 1")
    suspend fun getUserId(): Long?

    @Query("SELECT * FROM ${UserEntity.TABLE} LIMIT 1")
    fun observeUser(): Flow<UserEntity>

    @Update
    suspend fun update(user: UserEntity)

    @Query("DELETE FROM ${UserEntity.TABLE}")
    suspend fun clear()
}
