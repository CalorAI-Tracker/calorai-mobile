package dev.calorai.mobile.features.main.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserEntity.TABLE)
data class UserEntity(

    @PrimaryKey
    @ColumnInfo("user_id")
    val userId: Int,

    @ColumnInfo("sex")
    val sex: String,

    @ColumnInfo("height")
    val height: Int,

    @ColumnInfo("weight")
    val weight: Int,

    @ColumnInfo("birth_day")
    val birthDay: String,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("activity_code")
    val activityCode: String,

    @ColumnInfo("health_goal_code")
    val healthGoalCode: String,
) {
    companion object {
        const val TABLE = "user"
    }
}
