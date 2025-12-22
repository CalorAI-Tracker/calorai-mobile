package dev.calorai.mobile.features.main.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.calorai.mobile.features.main.features.settings.domain.GetUserProfileUseCase
import dev.calorai.mobile.features.main.features.settings.domain.SettingsException
import dev.calorai.mobile.features.main.features.settings.domain.UpdateUserHealthProfileUseCase
import dev.calorai.mobile.features.main.features.settings.data.SettingsMapper
import dev.calorai.mobile.features.main.features.settings.ui.model.SavingErrorType
import dev.calorai.mobile.features.main.features.settings.ui.model.UserProfileUi
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

class SettingsViewModel(
    private val updateUserHealthProfileUseCase: UpdateUserHealthProfileUseCase,
    private val getUserHealthProfileUseCase: GetUserProfileUseCase,
    private val mapper: SettingsMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    private val _uiActions = MutableSharedFlow<SettingsUiAction>()
    val uiActions: SharedFlow<SettingsUiAction> = _uiActions.asSharedFlow()

    init {
        loadUserProfile()
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            SettingsUiEvent.SaveButtonClick -> saveUserProfile()
            is SettingsUiEvent.ActivityChange -> launchUpdateUser { copy(activity = event.value) }
            is SettingsUiEvent.BirthDateChange -> updateBirthDate(event.selectedDateMillis)
            is SettingsUiEvent.EmailChange -> launchUpdateUser { copy(email = event.value) }
            is SettingsUiEvent.GenderChange -> launchUpdateUser { copy(gender = event.value) }
            is SettingsUiEvent.GoalChange -> launchUpdateUser { copy(goal = event.value) }
            is SettingsUiEvent.HeightChange -> launchUpdateUser { copy(height = event.value) }
            is SettingsUiEvent.NameChange -> launchUpdateUser { copy(name = event.value) }
            is SettingsUiEvent.WeightChange -> launchUpdateUser { copy(weight = event.value) }
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
            getUserHealthProfileUseCase(DEFAULT_USER_ID)?.let { user ->
                _state.update { it.copy(user = mapper.mapToUi(user)) }
            }
        }
    }

    private fun saveUserProfile() {
        val currentState = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            runCatching {
                updateUserHealthProfileUseCase.invoke(
                    userId = DEFAULT_USER_ID,
                    payload = mapper.mapToDomainPayload(currentState.user),
                )
            }
                .onSuccess {
                    _state.update { it.copy(isSaving = false) }
                    _uiActions.emit(SettingsUiAction.ShowSavingMessage)
                }
                .onFailure { error ->
                    _state.update { it.copy(isSaving = false) }
                    val (errorType, value) = when (error) {
                        is SettingsException.NumberParseError -> SavingErrorType.NUMBER_PARSE to null
                        is SettingsException.UnknownGender -> SavingErrorType.UNKNOWN_GENDER to error.value
                        is SettingsException.UnknownActivity -> SavingErrorType.UNKNOWN_ACTIVITY to error.value
                        is SettingsException.UnknownGoal -> SavingErrorType.UNKNOWN_GOAL to error.value
                        else -> SavingErrorType.SAVE_ERROR to null
                    }
                    _uiActions.emit(SettingsUiAction.ShowErrorMessage(errorType, value))
                }
        }
    }

    private fun launchUpdateUser(block: UserProfileUi.() -> UserProfileUi) {
        viewModelScope.launch {
            _state.update { it.copy(user = it.user.block()) }
        }
    }
}
