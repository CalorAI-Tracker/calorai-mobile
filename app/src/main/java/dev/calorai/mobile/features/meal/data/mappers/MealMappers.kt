package dev.calorai.mobile.features.meal.data.mappers

import dev.calorai.mobile.features.meal.data.dto.getDailyMeal.MealDto
import dev.calorai.mobile.features.meal.data.entity.DailyMealsEntity
import dev.calorai.mobile.features.meal.domain.model.DailyMeal
import java.time.LocalDate

fun MealDto.toEntity(date: String): DailyMealsEntity =
    DailyMealsEntity(
        id = 0L,
        date = date,
        meal = this.meal,
        kcal = this.kcal,
        proteinG = this.proteinG,
        fatG = this.fatG,
        carbsG = this.carbsG,
        entriesCnt = this.entriesCnt
    )

fun DailyMealsEntity.toDomain(): DailyMeal =
    DailyMeal(
        id = this.id,
        date = LocalDate.parse(this.date),
        meal = this.meal,
        kcal = this.kcal,
        proteinG = this.proteinG,
        fatG = this.fatG,
        carbsG = this.carbsG,
        entriesCnt = this.entriesCnt,
    )
