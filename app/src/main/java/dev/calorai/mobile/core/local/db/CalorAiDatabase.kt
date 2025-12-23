package dev.calorai.mobile.core.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.calorai.mobile.features.profile.data.dao.UserDao
import dev.calorai.mobile.features.profile.data.entity.UserEntity
import dev.calorai.mobile.features.meal.data.dao.DailyMealsDao
import dev.calorai.mobile.features.meal.data.entity.DailyMealsEntity

@Database(entities = [UserEntity::class, DailyMealsEntity::class], version = 1)
abstract class CalorAiDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun dailyMealDao(): DailyMealsDao
}