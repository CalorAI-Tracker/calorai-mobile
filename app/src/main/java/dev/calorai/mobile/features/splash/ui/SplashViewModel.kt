package dev.calorai.mobile.features.splash.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.auth.domain.RefreshTokensUseCase
import dev.calorai.mobile.features.auth.domain.UserHasAuthorizedUseCase
import dev.calorai.mobile.features.auth.login.navigateToLoginScreen
import dev.calorai.mobile.features.main.navigateToMainScreen
import dev.calorai.mobile.features.splash.SplashRoute
import kotlinx.coroutines.launch

class SplashViewModel constructor(
    private val globalRouter: Router,
    private val userHasAuthorizedUseCase: UserHasAuthorizedUseCase,
    private val refreshTokensUseCase: RefreshTokensUseCase,
) : ViewModel() {

    fun beginNavigation() {
        viewModelScope.launch {
            val isAuthorized = userHasAuthorizedUseCase.invoke()
            val options = NavOptions.Builder().setPopUpTo<SplashRoute>(inclusive = true).build()
            if (isAuthorized) {
                runCatching { refreshTokensUseCase.invoke() }
                    .onSuccess {
                        globalRouter.emit { navigateToMainScreen(navOptions = options) }
                    }.onFailure {
                        globalRouter.emit { navigateToLoginScreen(navOptions = options) }
                    }
            } else {
                globalRouter.emit { navigateToLoginScreen(navOptions = options) }
            }

        }
    }
}
