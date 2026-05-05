package dev.calorai.mobile.features.meal.data.dto.searchFoodCatalog

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchFoodCatalogRequest(
    @SerialName("pageSettings")
    val pageSettings: PageSettingsDto,
    @SerialName("search")
    val search: String,
)

@Serializable
data class PageSettingsDto(
    @SerialName("page")
    val page: Int,
    @SerialName("size")
    val size: Int,
)
