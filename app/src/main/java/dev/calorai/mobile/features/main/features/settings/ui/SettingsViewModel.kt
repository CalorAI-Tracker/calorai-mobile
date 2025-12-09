package dev.calorai.mobile.features.main.features.settings.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.calorai.mobile.R
import dev.calorai.mobile.features.main.features.settings.domain.UpdateUserHealthProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val DEFAULT_USER_ID = 1L // TODO заменить на реальный userId из авторизации

data class SettingsViewState(
    val form: UserSettingsUiState = UserSettingsUiState(),
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val saved: Boolean = false,
)

class SettingsViewModel(
    private val updateUserHealthProfileUseCase: UpdateUserHealthProfileUseCase,
    private val appContext: Context,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsViewState())
    val state: StateFlow<SettingsViewState> = _state

    fun onFormChange(form: UserSettingsUiState) {
        _state.update { it.copy(form = form, errorMessage = null, saved = false) }
    }

    fun onSave() {
        val currentForm = _state.value.form
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, errorMessage = null, saved = false) }
            val result = runCatching {
                updateUserHealthProfileUseCase(
                    userId = DEFAULT_USER_ID,
                    uiState = currentForm,
                )
            }
            result.onSuccess {
                _state.update { it.copy(isSaving = false, saved = true) }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.message ?: appContext.getString(R.string.settings_save_error),
                        saved = false,
                    )
                }
            }
        }
    }
}
