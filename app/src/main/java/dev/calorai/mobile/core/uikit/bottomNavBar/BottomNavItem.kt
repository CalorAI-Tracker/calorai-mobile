package dev.calorai.mobile.core.uikit.bottomNavBar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.calorai.mobile.R
import dev.calorai.mobile.features.main.features.home.HomeRoute
import dev.calorai.mobile.features.main.features.plan.PlanRoute
import dev.calorai.mobile.features.main.features.progress.ProgressRoute
import dev.calorai.mobile.features.main.features.settings.SettingsRoute
import kotlin.reflect.KClass

sealed class BottomNavItem(
    @StringRes val labelId: Int,
    @DrawableRes val iconId: Int,
    val route: KClass<*>,
) {

    data object Home : BottomNavItem(
        labelId = R.string.navbar_label_home,
        iconId = R.drawable.home_button,
        route = HomeRoute::class,
    )

    data object Plan : BottomNavItem(
        labelId = R.string.navbar_label_plan,
        iconId = R.drawable.plan_button,
        route = PlanRoute::class,
    )

    data object Progress : BottomNavItem(
        labelId = R.string.navbar_label_progress,
        iconId = R.drawable.progress_button,
        route = ProgressRoute::class,
    )

    data object Settings : BottomNavItem(
        labelId = R.string.navbar_label_settings,
        iconId = R.drawable.settings_button,
        route = SettingsRoute::class,
    )

    companion object {
        val ITEMS = listOf(
            Home,
            Plan,
            Progress,
            Settings,
        )
    }
}
