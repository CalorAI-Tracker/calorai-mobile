package dev.calorai.mobile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.calorai.mobile.features.auth.AuthRoute
import dev.calorai.mobile.features.auth.authSection
import dev.calorai.mobile.features.main.mainSection
import dev.calorai.mobile.features.main.navigateToMainScreen
import dev.calorai.mobile.features.meal.create.createMealSection
import dev.calorai.mobile.features.meal.details.mealDetailsSection

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
        mainSection(navController)
        createMealSection()
        mealDetailsSection()
    }
}
