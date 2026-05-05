package dev.calorai.mobile.features.meal.domain.model

data class FoodCatalogItem(
    val id: Long?,
    val name: String,
    val brand: String,
    val barcode: String,
    val provider: String,
    val kcalPer100g: Double,
    val proteinPer100g: Double,
    val fatPer100g: Double,
    val carbsPer100g: Double,
)

data class FoodCatalogSearchPage(
    val items: List<FoodCatalogItem>,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int,
)
