package dev.calorai.mobile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.calorai.mobile.auth.AuthRoute
import dev.calorai.mobile.auth.authSection
import dev.calorai.mobile.main.mainSection
import dev.calorai.mobile.main.navigateToMainScreen

@Composable
fun CalorAiNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoute,
        modifier = modifier,
    ) {
        authSection(
            navigateToAuthorizedZone = {
                navController.navigateToMainScreen(
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<AuthRoute>(inclusive = true)
                        .build()
                )
            },
        )
        mainSection()
    }
}
