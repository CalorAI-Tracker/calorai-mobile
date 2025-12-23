package dev.calorai.mobile.features.main.data.mappers

import dev.calorai.mobile.features.main.data.dto.userProfile.createUser.CreateUserProfileRequest
import dev.calorai.mobile.features.main.data.dto.userProfile.getUser.GetUserProfileResponse
import dev.calorai.mobile.features.main.data.dto.userProfile.updateUser.UpdateUserProfileResponse
import dev.calorai.mobile.features.main.data.entity.UserEntity
import dev.calorai.mobile.features.main.domain.model.User
import java.time.LocalDate

fun GetUserProfileResponse.toEntity(): UserEntity =
    UserEntity(
        userId = this.userId,
        sex = this.sex.name,
        height = this.height,
        weight = this.weight,
        birthDay = this.birthDay,
        name = this.name,
        activityCode = this.activityCode.name,
        healthGoalCode = this.healthGoalCode.name
    )

fun UpdateUserProfileResponse.toEntity(): UserEntity =
    UserEntity(
        userId = this.userId,
        sex = this.sex,
        height = this.height,
        weight = this.weight,
        birthDay = this.birthDay,
        name = this.name,
        activityCode = this.activityCode,
        healthGoalCode = this.healthGoalCode
    )

fun CreateUserProfileRequest.toEntity(): UserEntity =
    UserEntity(
        userId = this.userId,
        sex = this.sex.name,
        height = this.height,
        weight = this.weight,
        birthDay = this.birthDay,
        name = this.name,
        activityCode = this.activityCode.name,
        healthGoalCode = this.healthGoalCode.name
    )

fun UserEntity.toDomain(): User =
    User(
        userId = this.userId,
        name = this.name,
        sex = this.sex,
        height = this.height,
        weight = this.weight,
        birthDay = LocalDate.parse(this.birthDay),
        activityCode = this.activityCode,
        healthGoalCode = this.healthGoalCode,
    )
