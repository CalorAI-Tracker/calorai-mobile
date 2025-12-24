package dev.calorai.mobile.features.meal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DailyMealsEntity.TABLE)
data class DailyMealsEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0L,

    @ColumnInfo("date")
    val date: String,

    @ColumnInfo("meal")
    val meal: String,

    @ColumnInfo("kcal")
    val kcal: Int,

    @ColumnInfo("protein_g")
    val proteinG: String,

    @ColumnInfo("fat_g")
    val fatG: String,

    @ColumnInfo("carbs_g")
    val carbsG: String,

    @ColumnInfo("entries_cnt")
    val entriesCnt: Int,
) {
    companion object {
        const val TABLE = "daily_meals"
    }
}
