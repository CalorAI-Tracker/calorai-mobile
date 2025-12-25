package dev.calorai.mobile.features.auth.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.auth.domain.LoginUseCase
import dev.calorai.mobile.features.auth.login.LoginRoute
import dev.calorai.mobile.features.auth.signUp.navigateToSignUpScreen
import dev.calorai.mobile.features.main.navigateToMainScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel constructor(
    private val globalRouter: Router,
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> updateEmail(event.email)
            is LoginUiEvent.PasswordChanged -> updatePassword(event.password)
            LoginUiEvent.LoginButtonClick -> login()
            LoginUiEvent.GoogleLoginClick -> loginWithGoogle()
            LoginUiEvent.RegisterClick -> navigateToRegister()
        }
    }

    private fun updateEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    private fun updatePassword(password: String) {
        _state.update { it.copy(password = password) }
    }

    private fun login() {
        viewModelScope.launch {
            val currentState = _state.value
            runCatching {
                loginUseCase.invoke(
                    email = currentState.email,
                    password = currentState.password,
                )
            }
                .onSuccess {
                    globalRouter.emit {
                        navigateToMainScreen(
                            navOptions = NavOptions.Builder()
                                .setPopUpTo<LoginRoute>(inclusive = true)
                                .build()
                        )
                    }
                }
                .onFailure {
                    // TODO
                }
        }
    }

    private fun loginWithGoogle() {
        // Логика входа через Google
    }

    private fun navigateToRegister() {
        // Навигация к экрану регистрации
        viewModelScope.launch {
            globalRouter.emit {
                navigateToSignUpScreen(
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<LoginRoute>(inclusive = true)
                        .build()
                )
            }
        }
    }
}