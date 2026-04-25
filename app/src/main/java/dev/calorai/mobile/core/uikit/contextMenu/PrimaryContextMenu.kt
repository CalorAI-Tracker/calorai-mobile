package dev.calorai.mobile.core.uikit.contextMenu

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties

@Composable
fun <ITEM : PrimaryContextMenuItem<KEY>, KEY> PrimaryContextMenu(
    anchorOffset: Offset,
    items: List<ITEM>,
    onDismiss: () -> Unit,
    onItemSelected: (KEY) -> Unit,
    modifier: Modifier = Modifier,
) {
    Popup(
        popupPositionProvider = PrimaryContextMenuPositionProvider(
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
        PrimaryContextMenuContent(
            items = items,
            onItemSelected = { key ->
                onItemSelected(key)
                onDismiss()
            },
            modifier = modifier
                .width(210.dp)
        )
    }
}

@Composable
private fun <ITEM : PrimaryContextMenuItem<KEY>, KEY> PrimaryContextMenuContent(
    items: List<ITEM>,
    onItemSelected: (KEY) -> Unit,
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
                PrimaryContextMenuItem(
                    item = item,
                    onClick = { onItemSelected(item.key) },
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
private fun <ITEM : PrimaryContextMenuItem<KEY>, KEY> PrimaryContextMenuItem(
    item: ITEM,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
            text = stringResource(item.titleRes),
            style = MaterialTheme.typography.bodyMedium,
            color = item.titleColor,
        )
        Icon(
            painter = painterResource(item.iconRes),
            contentDescription = null,
            tint = item.iconColor,
            modifier = Modifier.size(20.dp),
        )
    }
}
