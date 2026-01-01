package dev.calorai.mobile.features.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.auth.domain.LogoutUseCase
import dev.calorai.mobile.features.auth.login.navigateToLoginScreen
import dev.calorai.mobile.features.main.MainRoute
import dev.calorai.mobile.features.profile.data.ProfileMapper
import dev.calorai.mobile.features.profile.domain.GetUserProfileUseCase
import dev.calorai.mobile.features.profile.domain.UpdateUserProfileUseCase
import dev.calorai.mobile.features.profile.domain.error.ProfileException
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

class ProfileViewModel constructor(
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val mapper: ProfileMapper,
    private val logoutUseCase: LogoutUseCase,
    private val globalRouter: Router,
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
            is ProfileUiEvent.ActivityChange -> launchUpdateUser { copy(activity = event.value) }
            is ProfileUiEvent.BirthDateChange -> updateBirthDate(event.selectedDateMillis)
            is ProfileUiEvent.EmailChange -> launchUpdateUser { copy(email = event.value) }
            is ProfileUiEvent.GenderChange -> launchUpdateUser { copy(gender = event.value) }
            is ProfileUiEvent.GoalChange -> launchUpdateUser { copy(goal = event.value) }
            is ProfileUiEvent.HeightChange -> launchUpdateUser { copy(height = event.value) }
            is ProfileUiEvent.NameChange -> launchUpdateUser { copy(name = event.value) }
            is ProfileUiEvent.WeightChange -> launchUpdateUser { copy(weight = event.value) }
            is ProfileUiEvent.OnRefresh -> loadUserProfile()
            ProfileUiEvent.LogoutClick -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            runCatching { logoutUseCase.invoke() }
                .onSuccess {
                    globalRouter.emit {
                        navigateToLoginScreen(
                            navOptions = NavOptions.Builder()
                                .setPopUpTo<MainRoute>(inclusive = true)
                                .build()
                        )
                    }
                }
        }
    }

    private fun updateBirthDate(selectedDateMillis: Long) {
        viewModelScope.launch {
            val instant = Instant.ofEpochMilli(selectedDateMillis)
            val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
            launchUpdateUser { copy(birthDate = mapper.mapLocalDateToUi(localDate)) }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true) }
            val result = runCatching {
                getUserProfileUseCase.invoke()?.let { user ->
                    _state.update { it.copy(user = mapper.mapToUi(user)) }
                }
            }
            _state.update { it.copy(isRefreshing = false) }
            if (result.isFailure) {
                _uiActions.emit(ProfileUiAction.ShowNetworkError)
            }
        }
    }

    private fun saveUserProfile() {
        val currentState = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            runCatching {
                updateUserProfileUseCase.invoke(mapper.mapToDomainPayload(currentState.user))
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
