package dev.calorai.mobile.core.uikit.ingredientContextMenu
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import dev.calorai.mobile.core.uikit.CalorAiTheme

@Composable
fun IngredientContextMenu(
    anchorOffset: Offset,
    onDismiss: () -> Unit,
    onItemSelected: (IngredientContextMenuAction) -> Unit,
) {
    Popup(
        popupPositionProvider = IngredientContextMenuPositionProvider(
            anchorOffset = anchorOffset,
        ),
        onDismissRequest = onDismiss,
        properties = PopupProperties(
            focusable = true,
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            clippingEnabled = true,
        ),
    ) {
        IngredientContextMenuContent(
            items = listOf(
                IngredientContextMenuItem.EditIngredientContextMenuItem,
                IngredientContextMenuItem.DeleteIngredientContextMenuItem
            ),
            onItemSelected = { action ->
                onItemSelected(action)
                onDismiss()
            },
            modifier = Modifier
                .width(210.dp)
        )
    }
}

@Composable
private fun IngredientContextMenuContent(
    items: List<IngredientContextMenuItem>,
    onItemSelected: (IngredientContextMenuAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
        ),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEachIndexed { index, item ->
                IngredientContextMenuItem(
                    item = item,
                    onClick = { onItemSelected(item.action) },
                )
                if (index != items.lastIndex) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                        thickness = 1.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun IngredientContextMenuItem(
    item: IngredientContextMenuItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = if (item.isDestructive) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 10.dp)
            .clickable{ onClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(item.title),
            style = MaterialTheme.typography.bodyMedium,
            color = color,
        )
        Icon(
            painter = painterResource(item.iconRes),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFFFEAE6,
)
@Composable
private fun MenuPreview() {
    val items = listOf(
        IngredientContextMenuItem.EditIngredientContextMenuItem,
        IngredientContextMenuItem.DeleteIngredientContextMenuItem
    )
    CalorAiTheme {
        IngredientContextMenuContent(
            items = items,
            onItemSelected = {},
            modifier = Modifier
                .width(230.dp)
        )
    }
}
