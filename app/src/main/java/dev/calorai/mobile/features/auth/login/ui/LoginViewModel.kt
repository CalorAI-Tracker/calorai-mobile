package dev.calorai.mobile.features.auth.login.ui

import androidx.credentials.Credential
import androidx.credentials.CustomCredential
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.auth.domain.LoginUseCase
import dev.calorai.mobile.features.auth.login.LoginRoute
import dev.calorai.mobile.features.auth.signUp.navigateToSignUpScreen
import dev.calorai.mobile.features.main.navigateToMainScreen
import dev.calorai.mobile.features.profile.domain.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel constructor(
    private val globalRouter: Router,
    private val loginUseCase: LoginUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    private val _uiActions = MutableSharedFlow<LoginUiAction>()
    val uiActions: SharedFlow<LoginUiAction> = _uiActions.asSharedFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChanged -> updateEmail(event.email)
            is LoginUiEvent.PasswordChanged -> updatePassword(event.password)
            LoginUiEvent.LoginButtonClick -> login()
            LoginUiEvent.GoogleLoginClick -> loginWithGoogle()
            LoginUiEvent.RegisterClick -> navigateToRegister()
            is LoginUiEvent.GoogleCredentials -> handleGoogleCredentials(event.credential)
            is LoginUiEvent.GoogleError -> handleGoogleError(event.error)
        }
    }

    private fun handleGoogleError(error: Exception) {
        TODO("Not yet implemented")
    }

    private fun handleGoogleCredentials(credential: Credential) {
        val responseJson: String
        when (credential) {

            // Passkey credential
            is PublicKeyCredential -> {
                // Share responseJson such as a GetCredentialResponse to your server to validate and
                // authenticate
                responseJson = credential.authenticationResponseJson
            }

            // Password credential
            is PasswordCredential -> {
                // Send ID and password to your server to validate and authenticate.
                val username = credential.id
                val password = credential.password

            }

            // GoogleIdToken credential
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        // Use googleIdTokenCredential and extract the ID to validate and
                        // authenticate on your server.
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        // You can use the members of googleIdTokenCredential directly for UX
                        // purposes, but don't use them to store or control access to user
                        // data. For that you first need to validate the token:
                        // pass googleIdTokenCredential.getIdToken() to the backend server.
                        // see [validation instructions](https://developers.google.com/identity/gsi/web/guides/verify-google-id-token)
                    } catch (e: GoogleIdTokenParsingException) {

                    }
                } else {
                    // Catch any unrecognized custom credential type here.

                }
            }

            else -> Unit
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
                    runCatching { getUserProfileUseCase.invoke() }
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
        viewModelScope.launch {
            _uiActions.emit(LoginUiAction.GoogleAuth)
        }
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