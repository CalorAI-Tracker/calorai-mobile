package dev.calorai.mobile.features.meal.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_meals")
data class DailyMealsEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long,

    val date: String,
    val meal: String,
    val kcal: Int,
    val proteinG: String,
    val fatG: String,
    val carbsG: String,
    val entriesCnt: Int,
)