package dev.calorai.mobile.features.auth.signUp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.auth.domain.LoginUseCase
import dev.calorai.mobile.features.auth.domain.SignUpUseCase
import dev.calorai.mobile.features.auth.login.LoginRoute
import dev.calorai.mobile.features.auth.login.navigateToLoginScreen
import dev.calorai.mobile.features.auth.signUp.SignUpRoute
import dev.calorai.mobile.features.main.navigateToMainScreen
import dev.calorai.mobile.features.profile.domain.CreateUserProfileUseCase
import dev.calorai.mobile.features.profile.domain.model.Activity
import dev.calorai.mobile.features.profile.domain.model.CreateUserProfilePayload
import dev.calorai.mobile.features.profile.domain.model.Gender
import dev.calorai.mobile.features.profile.domain.model.Goal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel constructor(
    private val globalRouter: Router,
    private val signUpUseCase: SignUpUseCase,
    private val loginUseCase: LoginUseCase,
    private val createUserProfileUseCase: CreateUserProfileUseCase,
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
        viewModelScope.launch {
            val currentState = _state.value
            val signUpResult = runCatching {
                signUpUseCase.invoke(
                    email = currentState.email,
                    password = currentState.password,
                )
            }
            if (signUpResult.isFailure) return@launch

            val loginResult = runCatching {
                loginUseCase.invoke(
                    email = currentState.email,
                    password = currentState.password,
                )
            }
            if (loginResult.isFailure) return@launch

            val createUserResult = runCatching {
                createUserProfileUseCase.invoke(
                    payload = CreateUserProfilePayload(
                        gender = Gender.MALE,
                        height = 180,
                        weight = 80,
                        birthDay = "24.07.2001",
                        name = currentState.name,
                        activityCode = Activity.ACTIVE,
                        healthGoalCode = Goal.MAINTAIN,
                    )
                )
            }
            if (createUserResult.isFailure) return@launch

            globalRouter.emit {
                navigateToMainScreen(
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<LoginRoute>(inclusive = true)
                        .build()
                )
            }
        }
    }

    private fun navigateToLogin() {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToLoginScreen(
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<SignUpRoute>(inclusive = true)
                        .build()
                )
            }
        }
    }
}
