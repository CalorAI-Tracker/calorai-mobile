package dev.calorai.mobile.features.main.domain.model

import dev.calorai.mobile.features.profile.domain.model.Activity
import dev.calorai.mobile.features.profile.domain.model.Gender
import dev.calorai.mobile.features.profile.domain.model.Goal
import java.time.LocalDate

data class User(

    val userId: Long,
    val email: String,
    val sex: Gender,
    val height: Int,
    val weight: Int,
    val birthDay: LocalDate,
    val name: String,
    val activityCode: Activity,
    val healthGoalCode: Goal,
)
