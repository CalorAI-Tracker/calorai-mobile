package dev.calorai.mobile.features.meal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.calorai.mobile.features.meal.data.entity.DailyMealsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyMealsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meal: DailyMealsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(meals: List<DailyMealsEntity>)

    @Query("SELECT * FROM ${DailyMealsEntity.TABLE} WHERE date = :date ORDER BY meal ASC")
    fun getMealsByDate(date: String): Flow<List<DailyMealsEntity>>

    @Query("SELECT * FROM ${DailyMealsEntity.TABLE} WHERE date = :date ORDER BY meal ASC")
    suspend fun getMealsByDateOnce(date: String): List<DailyMealsEntity>

    @Query("SELECT * FROM ${DailyMealsEntity.TABLE} WHERE date = :date AND meal = :mealType LIMIT 1")
    fun getMealByDateAndType(date: String, mealType: String): Flow<DailyMealsEntity?>

    @Query("DELETE FROM ${DailyMealsEntity.TABLE} WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM ${DailyMealsEntity.TABLE} WHERE date = :date")
    suspend fun deleteByDate(date: String)

    @Query("DELETE FROM ${DailyMealsEntity.TABLE}")
    suspend fun clearAllMeals()
}
