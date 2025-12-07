package dev.calorai.mobile.features.signUp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.auth.navigateToAuthScreen
import dev.calorai.mobile.features.signUp.SignUpRoute
import dev.calorai.mobile.features.main.navigateToMainScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel constructor(
    private val globalRouter: Router,
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpUiState())
    val state: StateFlow<SignUpUiState> = _state

    fun onEvent(event: SignUpUiEvent) {
        when (event) {
            is SignUpUiEvent.NameEntered -> updateName(event.name)
            is SignUpUiEvent.EmailEntered -> updateEmail(event.email)
            is SignUpUiEvent.PasswordEntered -> updatePassword(event.password)
            SignUpUiEvent.SignUpButtonClick -> register()
            SignUpUiEvent.LoginClick -> navigateToLogin()
        }
    }

    private fun updateName(name: String) {
        _state.update { it.copy(name = name) }
    }
    private fun updateEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    private fun register() {
        navigateToRegisteredZone()
    }


    private fun navigateToLogin() {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToAuthScreen(
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<SignUpRoute>(inclusive = true)
                        .build()
                )
            }
        }
    }

    private fun navigateToRegisteredZone() {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToMainScreen(
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<SignUpRoute>(inclusive = true)
                        .build()
                )
            }
        }
    }
}