package dev.calorai.mobile.core.uikit.ingredientContextMenu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.calorai.mobile.R

sealed class IngredientContextMenuItem(
    @StringRes val title: Int,
    @DrawableRes val iconRes: Int,
    val isDestructive: Boolean, // Делаем, чтобы пометить действие как необратимое. Потом можно добавить AlertDialog с подтверждением пользователем действия
    val action: IngredientContextMenuAction,
) {
    data object EditIngredientContextMenuItem: IngredientContextMenuItem(
        title = R.string.context_menu_edit,
        iconRes = R.drawable.ic_edit,
        isDestructive = false,
        action = IngredientContextMenuAction.EDIT,
    )

    data object DeleteIngredientContextMenuItem: IngredientContextMenuItem(
        title = R.string.context_menu_delete,
        iconRes = R.drawable.ic_delete,
        isDestructive = true,
        action = IngredientContextMenuAction.DELETE,
    )
}

enum class IngredientContextMenuAction {
    EDIT,
    DELETE,
}