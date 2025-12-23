package dev.calorai.mobile.features.profile.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.calorai.mobile.features.profile.data.dto.enums.ActivityCode
import dev.calorai.mobile.features.profile.data.dto.enums.HealthGoalCode
import dev.calorai.mobile.features.profile.data.dto.enums.Sex

@Entity(tableName = UserEntity.TABLE)
data class UserEntity(

    @PrimaryKey
    @ColumnInfo("user_id")
    val userId: Long,

    @ColumnInfo("sex")
    val sex: Sex,

    @ColumnInfo("height")
    val height: Int,

    @ColumnInfo("weight")
    val weight: Int,

    @ColumnInfo("birth_day")
    val birthDay: String,

    @ColumnInfo("name")
    val name: String,

    @ColumnInfo("email")
    val email: String,

    @ColumnInfo("activity_code")
    val activityCode: ActivityCode,

    @ColumnInfo("health_goal_code")
    val healthGoalCode: HealthGoalCode,
) {
    companion object {
        const val TABLE = "user"
    }
}
