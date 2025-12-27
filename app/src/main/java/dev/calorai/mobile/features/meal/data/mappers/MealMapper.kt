package dev.calorai.mobile.features.meal.data.mappers

import dev.calorai.mobile.core.uikit.mealCard.MealUiModel
import dev.calorai.mobile.core.uikit.pieChart.PieChartUiModel
import dev.calorai.mobile.core.uikit.pieChart.UnitOfMeasure
import dev.calorai.mobile.core.uikit.weekBar.DateUiModel
import dev.calorai.mobile.core.uikit.weekBar.TimePeriod
import dev.calorai.mobile.features.home.domain.model.DayMealProgressInfo
import dev.calorai.mobile.features.home.ui.model.MealTypeUi
import dev.calorai.mobile.features.home.ui.model.PieChartSubtextUi
import dev.calorai.mobile.features.meal.data.dto.createMealEntry.CreateMealEntryRequest
import dev.calorai.mobile.features.meal.data.dto.enums.MealTypeDto
import dev.calorai.mobile.features.meal.data.dto.getDailyMeal.GetDailyMealResponse
import dev.calorai.mobile.features.meal.data.dto.getDailyMeal.MealDto
import dev.calorai.mobile.features.meal.data.entity.DailyMealsEntity
import dev.calorai.mobile.features.meal.domain.model.CreateMealEntryPayload
import dev.calorai.mobile.features.meal.domain.model.DailyMeal
import dev.calorai.mobile.features.meal.domain.model.MealId
import dev.calorai.mobile.features.meal.domain.model.MealType
import java.time.LocalDate

class MealMapper {

    fun mapToRequest(payload: CreateMealEntryPayload): CreateMealEntryRequest =
        CreateMealEntryRequest(
            entryName = payload.entryName,
            meal = mapToData(payload.meal),
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
            date = date,
            meal = dto.meal.name,
            kcal = dto.kcal,
            proteinG = dto.proteinG,
            fatG = dto.fatG,
            carbsG = dto.carbsG,
            entriesCnt = dto.entriesCnt
        )

    fun mapToEntity(dailyMeals: List<DailyMeal>): List<DailyMealsEntity> {
        return dailyMeals.map { dailyMeal ->
            DailyMealsEntity(
                date = dailyMeal.date.toString(),
                meal = dailyMeal.meal.name,
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
            id = MealId(entity.id),
            date = LocalDate.parse(entity.date),
            meal = MealType.valueOf(entity.meal),
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
                id = MealId(0L),
                date = date,
                meal = mapToData(mealDto.meal),
                kcal = mealDto.kcal,
                proteinG = mealDto.proteinG,
                fatG = mealDto.fatG,
                carbsG = mealDto.carbsG,
                entriesCnt = mealDto.entriesCnt
            )
        }
    }

    fun mapToMealUiModel(dailyMeal: DailyMeal): MealUiModel =
        MealUiModel(
            id = dailyMeal.id.value,
            title = mapToUi(dailyMeal.meal),
            subtitleValue = dailyMeal.kcal,
            visibleFoodList = emptyList(),  // TODO: Потом исправить, когда сделаем составные приема пищи
            type = dailyMeal.meal,
        )

    fun mapToDateUiModel(dayProgress: DayMealProgressInfo): DateUiModel = DateUiModel(
        date = dayProgress.date,
        timePeriod = dayProgress.date.toTimePeriod(),
        progress = dayProgress.ratioKcal.first(),
    )

    fun mapToPieChartUiModel(dayInfo: DayMealProgressInfo): List<PieChartUiModel> = listOf(
        PieChartUiModel(
            targetValue = dayInfo.remainingAmountKcal,
            unitOfMeasure = UnitOfMeasure.NONE,
            targetSubtext = PieChartSubtextUi.KCAL.labelResId,
            leftText = "",
            pieData = dayInfo.ratioKcal,
        ),
        PieChartUiModel(
            targetValue = dayInfo.remainingAmountProtein,
            unitOfMeasure = UnitOfMeasure.GRAM,
            targetSubtext = PieChartSubtextUi.PROTEIN.labelResId,
            leftText = "",
            pieData = dayInfo.ratioProtein,
        ),
        PieChartUiModel(
            targetValue = dayInfo.remainingAmountFat,
            unitOfMeasure = UnitOfMeasure.GRAM,
            targetSubtext = PieChartSubtextUi.FAT.labelResId,
            leftText = "",
            pieData = dayInfo.ratioFat,
        ),
        PieChartUiModel(
            targetValue = dayInfo.remainingAmountCarbs,
            unitOfMeasure = UnitOfMeasure.GRAM,
            targetSubtext = PieChartSubtextUi.CARBS.labelResId,
            leftText = "",
            pieData = dayInfo.ratioCarbs,
        ),
    )

    private fun mapToData(mealType: MealType): MealTypeDto = when (mealType) {
        MealType.BREAKFAST -> MealTypeDto.BREAKFAST
        MealType.DINNER -> MealTypeDto.DINNER
        MealType.LUNCH -> MealTypeDto.LUNCH
        MealType.SNACK -> MealTypeDto.SNACK
    }

    private fun mapToData(mealType: MealTypeDto): MealType = when (mealType) {
        MealTypeDto.BREAKFAST -> MealType.BREAKFAST
        MealTypeDto.DINNER -> MealType.DINNER
        MealTypeDto.LUNCH -> MealType.LUNCH
        MealTypeDto.SNACK -> MealType.SNACK
    }

    private fun mapToUi(mealType: MealType): MealTypeUi = when (mealType) {
        MealType.BREAKFAST -> MealTypeUi.BREAKFAST
        MealType.DINNER -> MealTypeUi.DINNER
        MealType.LUNCH -> MealTypeUi.LUNCH
        MealType.SNACK -> MealTypeUi.SNACK
    }

    private fun LocalDate.toTimePeriod(referenceDate: LocalDate = LocalDate.now()): TimePeriod =
        when {
            isBefore(referenceDate) -> TimePeriod.PAST
            isAfter(referenceDate) -> TimePeriod.FUTURE
            else -> TimePeriod.PRESENT
        }
}
