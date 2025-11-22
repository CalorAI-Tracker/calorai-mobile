package dev.calorai.mobile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.calorai.mobile.core.navigation.GlobalRouterContext
import dev.calorai.mobile.core.navigation.RouterController
import dev.calorai.mobile.core.navigation.setupWith
import dev.calorai.mobile.features.auth.AuthRoute
import dev.calorai.mobile.features.auth.authSection
import dev.calorai.mobile.features.main.mainSection
import dev.calorai.mobile.features.meal.create.createMealSection
import dev.calorai.mobile.features.meal.details.mealDetailsSection
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
        startDestination = AuthRoute,
        modifier = modifier,
    ) {
        authSection()
        mainSection()
        createMealSection()
        mealDetailsSection()
    }
}
