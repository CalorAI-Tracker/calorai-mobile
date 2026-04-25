package dev.calorai.mobile.features.meal.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = IngredientsEntity.TABLE,
    indices = [
        Index(value = ["date", "meal_type"]),
    ],
)
data class IngredientsEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0L,

    @ColumnInfo("date")
    val date: String,

    @ColumnInfo("meal_type")
    val mealType: String,

    @ColumnInfo("entry_name")
    val entryName: String,

    @ColumnInfo("kcal")
    val kcal: Int,

    @ColumnInfo("protein_g")
    val proteinG: Double,

    @ColumnInfo("fat_g")
    val fatG: Double,

    @ColumnInfo("carbs_g")
    val carbsG: Double,

    @ColumnInfo("quantity_grams")
    val quantityGrams: Double,

) {
    companion object {
        const val TABLE = "meal_ingredients"
    }
}
