package dev.calorai.mobile.features.meal.domain

import dev.calorai.mobile.features.meal.domain.model.DailyMeal
import dev.calorai.mobile.features.meal.domain.model.MealEntry
import dev.calorai.mobile.features.meal.domain.model.MealEntryId
import dev.calorai.mobile.features.meal.domain.model.MealEntryPayload
import dev.calorai.mobile.features.meal.domain.model.MealId
import dev.calorai.mobile.features.meal.domain.model.MealRecognizeResult
import dev.calorai.mobile.features.meal.domain.model.MealType

interface MealRepository {

    suspend fun getDailyMeals(date: String): List<DailyMeal>
    suspend fun createMealEntryAndSync(payload: MealEntryPayload)
    suspend fun mealRecognize(image: ByteArray): MealRecognizeResult
    suspend fun getMealEntry(mealEntryId: MealEntryId): MealEntry
    suspend fun updateMealEntryAndSync(mealEntryId: MealEntryId, payload: MealEntryPayload)
    suspend fun deleteMealEntryAndSync(mealEntryId: MealEntryId, date: String)
    suspend fun getMealIngredients(date: String, mealType: MealType): List<MealEntry>
    suspend fun deleteMealById(id: MealId)
    suspend fun deleteMealsByDate(date: String)
    suspend fun deleteMeal(date: String, mealType: MealType)
    suspend fun clearAllMeals()
}
