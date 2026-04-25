package dev.calorai.mobile.core.uikit.contextMenu.mealContextMenu

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.contextMenu.PrimaryContextMenu

@Composable
fun MealContextMenu(
    anchorOffset: Offset,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    PrimaryContextMenu(
        anchorOffset = anchorOffset,
        items = listOf(
            MealContextMenuItem.DeleteMealContextMenuItem
        ),
        onDismiss = onDismiss,
        onItemSelected = { key: MealContextMenuItem.Key ->
            when (key) {
                MealContextMenuItem.Key.DELETE -> onDelete()
            }
        }
    )
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFEAE6,
)
@Composable
private fun MealContextMenuPreview() {
    CalorAiTheme {
        MealContextMenu(
            anchorOffset = Offset(0f, -250f),
            onDelete = {},
            onDismiss = {},
        )
    }
}
