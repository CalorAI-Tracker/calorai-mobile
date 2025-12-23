package dev.calorai.mobile.core.uikit.bottomNavBar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.calorai.mobile.R
import dev.calorai.mobile.features.home.HomeRoute
import dev.calorai.mobile.features.plan.PlanRoute
import dev.calorai.mobile.features.progress.ProgressRoute
import dev.calorai.mobile.features.profile.ProfileRoute
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

    data object Profile : BottomNavItem(
        labelId = R.string.navbar_label_profile,
        iconId = R.drawable.settings_button,
        route = ProfileRoute::class,
    )

    companion object {
        val ITEMS = listOf(
            Home,
            Plan,
            Progress,
            Profile,
        )
    }
}
