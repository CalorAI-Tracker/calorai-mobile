package dev.calorai.mobile.features.main.domain.model

import java.time.LocalDate

data class User(

    val userId: Int,
    val sex: String,
    val height: Int,
    val weight: Int,
    val birthDay: LocalDate,
    val name: String,
    val activityCode: String,
    val healthGoalCode: String,
)
