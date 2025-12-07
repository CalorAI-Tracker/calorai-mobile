package dev.calorai.mobile.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.auth.AuthRoute
import dev.calorai.mobile.features.main.navigateToMainScreen
import dev.calorai.mobile.features.signUp.navigateToSignUpScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel constructor(
    private val globalRouter: Router,
) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.EmailChanged -> updateEmail(event.email)
            is AuthUiEvent.PasswordChanged -> updatePassword(event.password)
            AuthUiEvent.LoginButtonClick -> login()
            AuthUiEvent.GoogleLoginClick -> loginWithGoogle()
            AuthUiEvent.RegisterClick -> navigateToRegister()
        }
    }

    private fun updateEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    private fun login() {
        // Здесь должна быть логика аутентификации
        navigateToAuthorizedZone()
    }

    private fun loginWithGoogle() {
        // Логика входа через Google
        navigateToAuthorizedZone()
    }

    private fun navigateToRegister() {
        // Навигация к экрану регистрации
        viewModelScope.launch {
            globalRouter.emit {
                navigateToSignUpScreen(
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<AuthRoute>(inclusive = true)
                        .build()
                )
            }
        }
    }

    private fun navigateToAuthorizedZone() {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToMainScreen(
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<AuthRoute>(inclusive = true)
                        .build()
                )
            }
        }
    }
}