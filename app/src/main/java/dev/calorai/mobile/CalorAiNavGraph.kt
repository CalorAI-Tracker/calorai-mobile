package dev.calorai.mobile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.navigation.setupWith
import dev.calorai.mobile.features.auth.login.loginSection
import dev.calorai.mobile.features.auth.signUp.signUpSection
import dev.calorai.mobile.features.main.mainSection
import dev.calorai.mobile.features.meal.create.manual.createMealSection
import dev.calorai.mobile.features.meal.details.mealDetailsSection
import dev.calorai.mobile.features.onboarding.onboardingSection
import dev.calorai.mobile.features.splash.SplashRoute
import dev.calorai.mobile.features.splash.splashSection
import org.koin.compose.koinInject
import org.koin.core.qualifier.qualifier

@Composable
fun CalorAiNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    globalRouterController: RouterController = koinInject(qualifier<GlobalRouterContext>()),
) {
    globalRouterController.setupWith(navController, GlobalRouterContext)
    NavHost(
        navController = navController,
        startDestination = SplashRoute,
        modifier = modifier,
    ) {
        splashSection()
        loginSection()
        signUpSection()
        mainSection()
        createMealSection()
        mealDetailsSection()
        onboardingSection()
    }
}
