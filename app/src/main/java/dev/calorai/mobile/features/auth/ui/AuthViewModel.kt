package dev.calorai.mobile.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.auth.AuthRoute
import dev.calorai.mobile.features.main.navigateToMainScreen
import kotlinx.coroutines.launch

class AuthViewModel constructor(
    private val globalRouter: Router,
) : ViewModel() {

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            AuthUiEvent.ButtonClick -> navigateToAuthorizedZone()
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
