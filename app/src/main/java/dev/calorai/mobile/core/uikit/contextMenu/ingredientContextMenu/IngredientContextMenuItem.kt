package dev.calorai.mobile.core.uikit.contextMenu.ingredientContextMenu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.contextMenu.PrimaryContextMenuItem

sealed class IngredientContextMenuItem(
    @StringRes override val titleRes: Int,
    @DrawableRes override val iconRes: Int,
    override val key: Key,
    private val isDestructive: Boolean,
) : PrimaryContextMenuItem<IngredientContextMenuItem.Key> {

    enum class Key {
        DELETE,
    }

    override val titleColor: Color
        @Composable get() = if (isDestructive) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.onPrimary
        }

    override val iconColor: Color
        @Composable get() = if (isDestructive) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.onPrimary
        }

    data object DeleteIngredientContextMenuItem: IngredientContextMenuItem(
        titleRes = R.string.context_menu_delete,
        iconRes = R.drawable.ic_delete,
        key = Key.DELETE,
        isDestructive = true,
    )
}
