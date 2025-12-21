package dev.calorai.mobile.features.main.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(

    @PrimaryKey
    val userId: Int,

    val sex: String,
    val height: Int,
    val weight: Int,
    val birthDay: String,
    val name: String,
    val activityCode: String,
    val healthGoalCode: String,
)
