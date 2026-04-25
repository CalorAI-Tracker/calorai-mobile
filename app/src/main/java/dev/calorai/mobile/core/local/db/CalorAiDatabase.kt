package dev.calorai.mobile.core.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.calorai.mobile.features.meal.data.dao.DailyMealsDao
import dev.calorai.mobile.features.meal.data.dao.IngredientsDao
import dev.calorai.mobile.features.meal.data.entity.DailyMealsEntity
import dev.calorai.mobile.features.meal.data.entity.IngredientsEntity
import dev.calorai.mobile.features.profile.data.dao.UserDao
import dev.calorai.mobile.features.profile.data.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        DailyMealsEntity::class,
        IngredientsEntity::class,
    ],
    version = 2,
    exportSchema = false,
)
abstract class CalorAiDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dailyMealDao(): DailyMealsDao
    abstract fun ingredientsDao(): IngredientsDao
}
