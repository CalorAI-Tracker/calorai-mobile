package dev.calorai.mobile.features.profile.data

import android.content.Context
import dev.calorai.mobile.core.utils.locale
import dev.calorai.mobile.features.profile.data.dto.createUser.CreateUserProfileRequest
import dev.calorai.mobile.features.profile.data.dto.enums.ActivityCode
import dev.calorai.mobile.features.profile.data.dto.enums.HealthGoalCode
import dev.calorai.mobile.features.profile.data.dto.enums.Sex
import dev.calorai.mobile.features.profile.data.dto.getUser.GetUserProfileResponse
import dev.calorai.mobile.features.profile.data.dto.updateUser.UpdateUserProfileRequest
import dev.calorai.mobile.features.profile.data.entity.UserEntity
import dev.calorai.mobile.features.profile.domain.error.ProfileException
import dev.calorai.mobile.features.profile.domain.model.Activity
import dev.calorai.mobile.features.profile.domain.model.CreateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.Gender
import dev.calorai.mobile.features.profile.domain.model.Goal
import dev.calorai.mobile.features.profile.domain.model.UpdateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.UserId
import dev.calorai.mobile.features.profile.domain.model.UserProfile
import dev.calorai.mobile.features.profile.ui.model.ActivityUi
import dev.calorai.mobile.features.profile.ui.model.GenderUi
import dev.calorai.mobile.features.profile.ui.model.GoalUi
import dev.calorai.mobile.features.profile.ui.model.UserProfileUi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProfileMapper(
    private val context: Context,
) {

    private val uiDateFormatter by lazy {
        DateTimeFormatter.ofPattern("dd MMMM yyyy", context.locale)
    }

    private val domainDateFormatter by lazy {
        DateTimeFormatter.ofPattern("dd.MM.yyyy", context.locale)
    }

    private fun mapBirthdayToUi(date: String): String {
        val localDate = LocalDate.parse(date, domainDateFormatter)
        return localDate.format(uiDateFormatter)
    }

    fun mapLocalDateToUi(localDate: LocalDate): String = localDate.format(uiDateFormatter)

    fun mapBirthdayToDomain(date: String): String {
        val localDate = LocalDate.parse(date, uiDateFormatter)
        return localDate.format(domainDateFormatter)
    }

    fun mapToUi(user: UserProfile): UserProfileUi = UserProfileUi(
        name = user.name,
        email = user.email,
        birthDate = mapBirthdayToUi(user.birthDay),
        gender = mapToUi(user.gender),
        height = user.height,
        weight = user.weight,
        activity = mapToUi(user.activityCode),
        goal = mapToUi(user.healthGoalCode),
    )

    fun mapToDomainPayload(uiState: UserProfileUi): UpdateUserProfilePayload =
        UpdateUserProfilePayload(
            name = uiState.name.ifEmpty { throw ProfileException.EmptyField() },
            email = uiState.email.ifEmpty { throw ProfileException.EmptyField() },
            gender = mapToDomain(uiState.gender),
            height = uiState.height.extractNumberOrThrow(),
            weight = uiState.weight.extractNumberOrThrow(),
            birthDay = mapBirthdayToDomain(
                date = uiState.birthDate.ifEmpty { throw ProfileException.BirthDateParseError() },
            ),
            activityCode = mapToDomain(uiState.activity),
            healthGoalCode = mapToDomain(uiState.goal),
            targetWeightKg = null,
            weeklyRateKg = null,
        )

    fun mapToEntity(profile: UserProfile): UserEntity = UserEntity(
        userId = profile.userId.value,
        sex = mapToData(profile.gender),
        height = profile.height,
        weight = profile.weight,
        birthDay = profile.birthDay,
        name = profile.name,
        email = profile.email,
        activityCode = mapToData(profile.activityCode),
        healthGoalCode = mapToData(profile.healthGoalCode),
    )

    fun mapToEntity(userId: UserId, payload: UpdateUserProfilePayload): UserEntity = UserEntity(
        userId = userId.value,
        sex = mapToData(payload.gender),
        height = payload.height,
        weight = payload.weight,
        birthDay = payload.birthDay,
        name = payload.name,
        email = payload.email,
        activityCode = mapToData(payload.activityCode),
        healthGoalCode = mapToData(payload.healthGoalCode),
    )

    fun mapToEntity(userId: UserId, payload: CreateUserProfilePayload): UserEntity = UserEntity(
        userId = userId.value,
        sex = mapToData(payload.gender),
        height = payload.height,
        weight = payload.weight,
        birthDay = payload.birthDay,
        name = payload.name,
        email = "",
        activityCode = mapToData(payload.activityCode),
        healthGoalCode = mapToData(payload.healthGoalCode),
    )

    fun mapToDomain(userProfileResponse: GetUserProfileResponse): UserProfile = UserProfile(
        userId = UserId(userProfileResponse.userId),
        name = userProfileResponse.name,
        email = userProfileResponse.email,
        gender = mapToDomain(userProfileResponse.sex),
        height = userProfileResponse.height,
        weight = userProfileResponse.weight,
        birthDay = userProfileResponse.birthDay,
        activityCode = mapToDomain(userProfileResponse.activityCode),
        healthGoalCode = mapToDomain(userProfileResponse.healthGoalCode),
        targetWeightKg = null,
        weeklyRateKg = null,
    )

    fun mapToDomain(entity: UserEntity): UserProfile = UserProfile(
        userId = UserId(entity.userId),
        name = entity.name,
        email = entity.email,
        gender = mapToDomain(entity.sex),
        height = entity.height,
        weight = entity.weight,
        birthDay = entity.birthDay,
        activityCode = mapToDomain(entity.activityCode),
        healthGoalCode = mapToDomain(entity.healthGoalCode),
        targetWeightKg = null,
        weeklyRateKg = null,
    )

    fun mapToRequest(payload: UpdateUserProfilePayload): UpdateUserProfileRequest =
        UpdateUserProfileRequest(
            email = payload.email,
            name = payload.name,
            sex = mapToData(payload.gender),
            height = payload.height,
            weight = payload.weight,
            birthDay = payload.birthDay,
            activityCode = mapToData(payload.activityCode),
            healthGoalCode = mapToData(payload.healthGoalCode),
        )

    fun mapToRequest(userId: UserId, payload: CreateUserProfilePayload): CreateUserProfileRequest =
        CreateUserProfileRequest(
            userId = userId.value,
            sex = mapToData(payload.gender),
            height = payload.height,
            weight = payload.weight,
            birthDay = payload.birthDay,
            name = payload.name,
            activityCode = mapToData(payload.activityCode),
            healthGoalCode = mapToData(payload.healthGoalCode),
        )

    private fun mapToData(goal: Goal): HealthGoalCode = when (goal) {
        Goal.LOSE_WEIGHT -> HealthGoalCode.LOSE_WEIGHT
        Goal.MAINTAIN -> HealthGoalCode.MAINTAIN
        Goal.GAIN_MUSCLE -> HealthGoalCode.GAIN_MUSCLE
        Goal.GAIN_WEIGHT -> HealthGoalCode.GAIN_WEIGHT
    }

    private fun mapToData(activityCode: Activity): ActivityCode = when (activityCode) {
        Activity.SEDENTARY -> ActivityCode.SEDENTARY
        Activity.LIGHT -> ActivityCode.LIGHT
        Activity.MODERATE -> ActivityCode.MODERATE
        Activity.ACTIVE -> ActivityCode.ACTIVE
        Activity.VERY_ACTIVE -> ActivityCode.VERY_ACTIVE
    }

    private fun mapToData(gender: Gender): Sex = when (gender) {
        Gender.FEMALE -> Sex.F
        Gender.MALE -> Sex.M
    }

    private fun mapToUi(activity: Activity): ActivityUi = when (activity) {
        Activity.SEDENTARY -> ActivityUi.SEDENTARY
        Activity.LIGHT -> ActivityUi.LIGHT
        Activity.MODERATE -> ActivityUi.MODERATE
        Activity.ACTIVE -> ActivityUi.ACTIVE
        Activity.VERY_ACTIVE -> ActivityUi.VERY_ACTIVE
    }

    private fun mapToUi(gender: Gender): GenderUi = when (gender) {
        Gender.FEMALE -> GenderUi.FEMALE
        Gender.MALE -> GenderUi.MALE
    }

    private fun mapToUi(goal: Goal): GoalUi = when (goal) {
        Goal.LOSE_WEIGHT -> GoalUi.LOSE_WEIGHT
        Goal.GAIN_WEIGHT -> GoalUi.GAIN_WEIGHT
        Goal.GAIN_MUSCLE -> GoalUi.GAIN_MUSCLE
        Goal.MAINTAIN -> GoalUi.MAINTAIN
    }

    private fun mapToDomain(activity: ActivityUi?): Activity = when (activity) {
        ActivityUi.SEDENTARY -> Activity.SEDENTARY
        ActivityUi.LIGHT -> Activity.LIGHT
        ActivityUi.MODERATE -> Activity.MODERATE
        ActivityUi.ACTIVE -> Activity.ACTIVE
        ActivityUi.VERY_ACTIVE -> Activity.VERY_ACTIVE
        else -> throw ProfileException.UnknownActivity(activity.toString())
    }

    private fun mapToDomain(sex: GenderUi?): Gender = when (sex) {
        GenderUi.FEMALE -> Gender.FEMALE
        GenderUi.MALE -> Gender.MALE
        else -> throw ProfileException.UnknownGender(sex.toString())
    }

    private fun mapToDomain(goal: GoalUi?): Goal = when (goal) {
        GoalUi.LOSE_WEIGHT -> Goal.LOSE_WEIGHT
        GoalUi.MAINTAIN -> Goal.MAINTAIN
        GoalUi.GAIN_WEIGHT -> Goal.GAIN_WEIGHT
        GoalUi.GAIN_MUSCLE -> Goal.GAIN_MUSCLE
        else -> throw ProfileException.UnknownGoal(goal.toString())
    }

    private fun mapToDomain(activity: ActivityCode): Activity = when (activity) {
        ActivityCode.SEDENTARY -> Activity.SEDENTARY
        ActivityCode.LIGHT -> Activity.LIGHT
        ActivityCode.MODERATE -> Activity.MODERATE
        ActivityCode.ACTIVE -> Activity.ACTIVE
        ActivityCode.VERY_ACTIVE -> Activity.VERY_ACTIVE
    }

    private fun mapToDomain(sex: Sex): Gender = when (sex) {
        Sex.F -> Gender.FEMALE
        Sex.M -> Gender.MALE
    }

    private fun mapToDomain(goal: HealthGoalCode): Goal = when (goal) {
        HealthGoalCode.LOSE_WEIGHT -> Goal.LOSE_WEIGHT
        HealthGoalCode.MAINTAIN -> Goal.MAINTAIN
        HealthGoalCode.GAIN_WEIGHT -> Goal.GAIN_WEIGHT
        HealthGoalCode.GAIN_MUSCLE -> Goal.GAIN_MUSCLE
    }

    private fun Int?.extractNumberOrThrow(): Int {
        return this ?: throw ProfileException.NumberParseError()
    }
}
