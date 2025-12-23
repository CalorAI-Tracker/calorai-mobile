package dev.calorai.mobile.features.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.calorai.mobile.features.profile.domain.GetUserProfileUseCase
import dev.calorai.mobile.features.profile.domain.error.ProfileException
import dev.calorai.mobile.features.profile.domain.UpdateUserProfileUseCase
import dev.calorai.mobile.features.profile.data.ProfileMapper
import dev.calorai.mobile.features.profile.ui.model.SavingErrorType
import dev.calorai.mobile.features.profile.ui.model.UserProfileUi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

private const val DEFAULT_USER_ID = 1L // TODO заменить на реальный userId из авторизации

class ProfileViewModel(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getUserHealthProfileUseCase: GetUserProfileUseCase,
    private val mapper: ProfileMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    private val _uiActions = MutableSharedFlow<ProfileUiAction>()
    val uiActions: SharedFlow<ProfileUiAction> = _uiActions.asSharedFlow()

    init {
        loadUserProfile()
    }

    fun onEvent(event: ProfileUiEvent) {
        when (event) {
            ProfileUiEvent.SaveButtonClick -> saveUserProfile()
            is ProfileUiEvent.ActivityChange -> launchUpdateUser { UserProfileUi(activity = event.value) }
            is ProfileUiEvent.BirthDateChange -> updateBirthDate(event.selectedDateMillis)
            is ProfileUiEvent.EmailChange -> launchUpdateUser { UserProfileUi(email = event.value) }
            is ProfileUiEvent.GenderChange -> launchUpdateUser { UserProfileUi(gender = event.value) }
            is ProfileUiEvent.GoalChange -> launchUpdateUser { UserProfileUi(goal = event.value) }
            is ProfileUiEvent.HeightChange -> launchUpdateUser { UserProfileUi(height = event.value) }
            is ProfileUiEvent.NameChange -> launchUpdateUser { UserProfileUi(name = event.value) }
            is ProfileUiEvent.WeightChange -> launchUpdateUser { UserProfileUi(weight = event.value) }
            is ProfileUiEvent.OnRefresh -> loadUserProfile()
        }
    }

    private fun updateBirthDate(selectedDateMillis: Long) {
        viewModelScope.launch {
            val instant = Instant.ofEpochMilli(selectedDateMillis)
            val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            launchUpdateUser { UserProfileUi(birthDate = mapper.mapLocalDateToUi(localDate)) }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            runCatching {
                getUserHealthProfileUseCase(DEFAULT_USER_ID)?.let { user ->
                    _state.update { it.copy(user = mapper.mapToUi(user)) }
                }
            }.onFailure {
                _uiActions.emit(ProfileUiAction.ShowNetworkError)
            }
        }
    }

    private fun saveUserProfile() {
        val currentState = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            runCatching {
                updateUserProfileUseCase.invoke(
                    userId = DEFAULT_USER_ID,
                    payload = mapper.mapToDomainPayload(currentState.user),
                )
            }
                .onSuccess {
                    _state.update { it.copy(isSaving = false) }
                    _uiActions.emit(ProfileUiAction.ShowSavingMessage)
                }
                .onFailure { error ->
                    _state.update { it.copy(isSaving = false) }
                    val (errorType, value) = when (error) {
                        is ProfileException.NumberParseError -> SavingErrorType.NUMBER_PARSE to null
                        is ProfileException.UnknownGender -> SavingErrorType.UNKNOWN_GENDER to error.value
                        is ProfileException.UnknownActivity -> SavingErrorType.UNKNOWN_ACTIVITY to error.value
                        is ProfileException.UnknownGoal -> SavingErrorType.UNKNOWN_GOAL to error.value
                        else -> SavingErrorType.SAVE_ERROR to null
                    }
                    _uiActions.emit(ProfileUiAction.ShowErrorMessage(errorType, value))
                }
        }
    }

    private fun launchUpdateUser(block: UserProfileUi.() -> UserProfileUi) {
        viewModelScope.launch {
            _state.update { it.copy(user = it.user.block()) }
        }
    }
}
