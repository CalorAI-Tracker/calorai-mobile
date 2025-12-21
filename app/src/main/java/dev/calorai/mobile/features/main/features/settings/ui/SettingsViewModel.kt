package dev.calorai.mobile.features.main.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.calorai.mobile.features.main.features.settings.domain.GetUserHealthProfileUseCase
import dev.calorai.mobile.features.main.features.settings.domain.SettingsException
import dev.calorai.mobile.features.main.features.settings.domain.UpdateUserHealthProfileUseCase
import dev.calorai.mobile.features.main.features.settings.data.SettingsMapper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val DEFAULT_USER_ID = 1L // TODO заменить на реальный userId из авторизации

class SettingsViewModel(
    private val updateUserHealthProfileUseCase: UpdateUserHealthProfileUseCase,
    private val getUserHealthProfileUseCase: GetUserHealthProfileUseCase,
    private val mapper: SettingsMapper,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsViewState())
    val state: StateFlow<SettingsViewState> = _state.asStateFlow()

    private val _uiActions = MutableSharedFlow<SettingsUiAction>()
    val uiActions: SharedFlow<SettingsUiAction> = _uiActions.asSharedFlow()

    init {
        loadUserProfile()
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.FormChange -> {
                _state.update { it.copy(form = event.form) }
            }
            SettingsUiEvent.Save -> {
                saveUserProfile()
            }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            getUserHealthProfileUseCase(DEFAULT_USER_ID)?.let { profile ->
                val uiState = mapper.mapToUiState(profile)
                _state.update { it.copy(form = uiState) }
            }
        }
    }

    private fun saveUserProfile() {
        val currentForm = _state.value.form
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            val result = runCatching {
                val payload = mapper.mapToDomainPayload(currentForm)
                updateUserHealthProfileUseCase(
                    userId = DEFAULT_USER_ID,
                    payload = payload,
                )
            }
            result.onSuccess {
                _state.update { it.copy(isSaving = false) }
                _uiActions.emit(SettingsUiAction.ShowSavingMessage)
            }.onFailure { error ->
                _state.update { it.copy(isSaving = false) }
                val (errorType, value) = when (error) {
                    is SettingsException.NumberParseError -> SavingErrorType.NUMBER_PARSE to error.value
                    is SettingsException.UnknownGender -> SavingErrorType.UNKNOWN_GENDER to error.value
                    is SettingsException.UnknownActivity -> SavingErrorType.UNKNOWN_ACTIVITY to error.value
                    is SettingsException.UnknownGoal -> SavingErrorType.UNKNOWN_GOAL to error.value
                    else -> SavingErrorType.SAVE_ERROR to null
                }
                _uiActions.emit(SettingsUiAction.ShowErrorMessage(errorType, value))
            }
        }
    }
}
