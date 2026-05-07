package dev.calorai.mobile.features.meal.ready.ui

import dev.calorai.mobile.features.meal.domain.model.FoodCatalogItem

class MealReadyUiMapper {

    fun mapToReadyMealUi(
        item: FoodCatalogItem,
        id: String,
        summary: String,
    ): ReadyMealUi = ReadyMealUi(
        id = id,
        title = item.name,
        brand = item.brand,
        barcode = item.barcode,
        summary = summary,
    )
}
