package dev.calorai.mobile.core.uikit.contextMenu.ingredientContextMenu

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.contextMenu.PrimaryContextMenu

@Composable
fun IngredientContextMenu(
    anchorOffset: Offset,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    PrimaryContextMenu(
        anchorOffset = anchorOffset,
        items = listOf(
            IngredientContextMenuItem.EditIngredientContextMenuItem,
            IngredientContextMenuItem.DeleteIngredientContextMenuItem
        ),
        onDismiss = onDismiss,
        onItemSelected = { key: IngredientContextMenuItem.Key ->
            when (key) {
                IngredientContextMenuItem.Key.EDIT -> onEdit()
                IngredientContextMenuItem.Key.DELETE -> onDelete()
            }
        }
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFEAE6,
)
@Composable
private fun IngredientContextMenuPreview() {
    CalorAiTheme {
        IngredientContextMenu(
            anchorOffset = Offset(0f, -250f),
            onEdit = {},
            onDelete = {},
            onDismiss = {},
        )
    }
}
