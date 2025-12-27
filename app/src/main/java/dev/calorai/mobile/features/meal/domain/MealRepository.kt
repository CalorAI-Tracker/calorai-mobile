package dev.calorai.mobile.features.meal.domain

import dev.calorai.mobile.features.meal.domain.model.CreateMealEntryPayload
import dev.calorai.mobile.features.meal.domain.model.DailyMeal
import dev.calorai.mobile.features.meal.domain.model.MealId
import kotlinx.coroutines.flow.Flow

interface MealRepository {

    suspend fun getDailyMeals(date: String): List<DailyMeal>
    suspend fun createMealEntryAndSync(payload: CreateMealEntryPayload)
    fun observeMealsByDate(date: String): Flow<List<DailyMeal>>
    suspend fun deleteMealById(id: MealId)
    suspend fun deleteMealsByDate(date: String)
    suspend fun clearAllMeals()
}
