package dev.calorai.mobile.features.meal.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.calorai.mobile.features.meal.data.entity.IngredientsEntity

@Dao
interface IngredientsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ingredients: List<IngredientsEntity>)

    @Query("SELECT * FROM ${IngredientsEntity.TABLE} WHERE date = :date")
    suspend fun getByDateOnce(date: String): List<IngredientsEntity>

    @Query("SELECT * FROM ${IngredientsEntity.TABLE} WHERE date = :date AND meal_type = :mealType ORDER BY id ASC")
    suspend fun getByDateAndMealTypeOnce(date: String, mealType: String): List<IngredientsEntity>

    @Query("SELECT * FROM ${IngredientsEntity.TABLE} WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): IngredientsEntity?

    @Query("DELETE FROM ${IngredientsEntity.TABLE} WHERE date = :date")
    suspend fun deleteByDate(date: String)

    @Query("DELETE FROM ${IngredientsEntity.TABLE} WHERE date = :date AND meal_type = :mealType")
    suspend fun deleteByDateAndMealType(date: String, mealType: String)

    @Query("DELETE FROM ${IngredientsEntity.TABLE}")
    suspend fun clearAll()
}
