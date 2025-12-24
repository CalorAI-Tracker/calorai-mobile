package dev.calorai.mobile.features.meal.data.mappers

import dev.calorai.mobile.features.meal.data.dto.createMealEntry.CreateMealEntryRequest
import dev.calorai.mobile.features.meal.data.dto.getDailyMeal.GetDailyMealResponse
import dev.calorai.mobile.features.meal.data.dto.getDailyMeal.MealDto
import dev.calorai.mobile.features.meal.data.entity.DailyMealsEntity
import dev.calorai.mobile.features.meal.domain.model.CreateMealEntryPayload
import dev.calorai.mobile.features.meal.domain.model.DailyMeal
import java.time.LocalDate

class MealMapper {

    fun mapToRequest(payload: CreateMealEntryPayload): CreateMealEntryRequest =
        CreateMealEntryRequest(
            entryName = payload.entryName,
            meal = payload.meal,
            eatenAt = payload.eatenAt,
            proteinPerBaseG = payload.proteinPerBaseG,
            fatPerBaseG = payload.fatPerBaseG,
            carbsPerBaseG = payload.carbsPerBaseG,
            baseQuantityGrams = payload.baseQuantityGrams,
            portionQuantityGrams = payload.portionQuantityGrams,
            brand = payload.brand,
            barcode = payload.barcode,
            note = payload.note,
        )

    fun mapToEntity(dto: MealDto, date: String): DailyMealsEntity =
        DailyMealsEntity(
            id = 0L,
            date = date,
            meal = dto.meal,
            kcal = dto.kcal,
            proteinG = dto.proteinG,
            fatG = dto.fatG,
            carbsG = dto.carbsG,
            entriesCnt = dto.entriesCnt
        )

    fun mapToEntity(dailyMeals: List<DailyMeal>): List<DailyMealsEntity> {
        return dailyMeals.map { dailyMeal ->
            DailyMealsEntity(
                id = 0L,
                date = dailyMeal.date.toString(),
                meal = dailyMeal.meal,
                kcal = dailyMeal.kcal,
                proteinG = dailyMeal.proteinG,
                fatG = dailyMeal.fatG,
                carbsG = dailyMeal.carbsG,
                entriesCnt = dailyMeal.entriesCnt
            )
        }
    }

    fun mapToDomain(entity: DailyMealsEntity): DailyMeal =
        DailyMeal(
            id = entity.id,
            date = LocalDate.parse(entity.date),
            meal = entity.meal,
            kcal = entity.kcal,
            proteinG = entity.proteinG,
            fatG = entity.fatG,
            carbsG = entity.carbsG,
            entriesCnt = entity.entriesCnt,
        )

    fun mapToDomain(response: GetDailyMealResponse): List<DailyMeal> {
        val date = LocalDate.parse(response.date)
        return response.meals.map { mealDto ->
            DailyMeal(
                id = 0L,
                date = date,
                meal = mealDto.meal,
                kcal = mealDto.kcal,
                proteinG = mealDto.proteinG,
                fatG = mealDto.fatG,
                carbsG = mealDto.carbsG,
                entriesCnt = mealDto.entriesCnt
            )
        }
    }
}
