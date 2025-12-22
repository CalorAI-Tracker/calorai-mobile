package dev.calorai.mobile.features.main.data.mappers

import dev.calorai.mobile.features.main.data.dto.userProfile.createUser.CreateUserProfileRequest
import dev.calorai.mobile.features.main.data.dto.userProfile.enums.ActivityCode
import dev.calorai.mobile.features.main.data.dto.userProfile.enums.HealthGoalCode
import dev.calorai.mobile.features.main.data.dto.userProfile.enums.Sex
import dev.calorai.mobile.features.main.data.dto.userProfile.getUser.GetUserProfileResponse
import dev.calorai.mobile.features.main.data.dto.userProfile.updateUser.UpdateUserProfileResponse
import dev.calorai.mobile.features.main.data.entity.UserEntity

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

fun UserEntity.toGetUserProfileResponse(): GetUserProfileResponse =
    GetUserProfileResponse(
        userId = this.userId,
        name = this.name,
        email = "",
        sex = Sex.valueOf(sex),
        height = this.height,
        weight = this.weight,
        birthDay = this.birthDay,
        activityCode = ActivityCode.valueOf(activityCode),
        healthGoalCode = HealthGoalCode.valueOf(healthGoalCode),
    )
