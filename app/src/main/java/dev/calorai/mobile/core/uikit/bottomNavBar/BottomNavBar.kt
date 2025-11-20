package dev.calorai.mobile.core.uikit.bottomNavBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.circleMediumSize

@Composable
fun BottomNavBar(
    selectedItem: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    onFabClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFabButtonEnabled = selectedItem == BottomNavItem.Home
    val fabButtonOffset = 4.dp
    val fabButtonSize = circleMediumSize
    val navBarTopMargin = 20.dp
    val fabButtonOuterCornerRadius = 14.dp
    val fabButtonInnerCornerRadius = 32.dp
    val animatedSize: Dp by animateDpAsState(
        if (isFabButtonEnabled) fabButtonSize else 0.dp
    )
    val animatedRadius: Dp by animateDpAsState(
        if (isFabButtonEnabled) fabButtonOuterCornerRadius else 0.dp,
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        BottomNavBackground(
            fabButtonTopMargin = fabButtonOffset,
            fabButtonSize = animatedSize,
            navBarTopMargin = navBarTopMargin,
            fabButtonInnerCornerRadius = fabButtonInnerCornerRadius,
            fabButtonOuterCornerRadius = animatedRadius,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f),
            ) {
                listOf(BottomNavItem.Home, BottomNavItem.Plan).forEach { item ->
                    BottomNavItem(
                        item = item,
                        isSelected = selectedItem == item,
                        onClick = { onItemSelected(item) },
                        modifier = Modifier
                            .wrapContentWidth()
                            .defaultMinSize(minWidth = 64.dp),
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f),
            ) {
                listOf(BottomNavItem.Progress, BottomNavItem.Settings).forEach { item ->
                    BottomNavItem(
                        item = item,
                        isSelected = selectedItem == item,
                        onClick = { onItemSelected(item) },
                        modifier = Modifier
                            .wrapContentWidth()
                            .defaultMinSize(minWidth = 64.dp),
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = isFabButtonEnabled,
            enter = fadeIn(animationSpec = tween(400)),
            exit = fadeOut(animationSpec = tween(200)),
            modifier = Modifier
                .size(fabButtonSize)
                .align(Alignment.TopCenter)
                .offset(y = fabButtonOffset, x = 0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.add_button_red),
                contentDescription = stringResource(R.string.add),
                modifier = Modifier
                    .clip(RoundedCornerShape(60.dp))
                    .clickable(onClick = onFabClick),
            )
        }
    }
}

@Composable
private fun BottomNavBackground(
    fabButtonTopMargin: Dp,
    fabButtonSize: Dp,
    fabButtonOuterCornerRadius: Dp,
    fabButtonInnerCornerRadius: Dp,
    navBarTopMargin: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.drawBehind {
            clipPath(
                path = fabButtonClipPath(
                    fabButtonSize = fabButtonSize,
                    navBarTopMargin = navBarTopMargin,
                    fabButtonTopMargin = fabButtonTopMargin,
                    fabButtonSizeOffset = 3.dp,
                    outerCornerRadius = fabButtonOuterCornerRadius.toPx(),
                    innerCornerRadius = fabButtonInnerCornerRadius.toPx(),
                ),
                clipOp = ClipOp.Difference,
            ) {
                drawNavBarMainRectangle(
                    topMargin = navBarTopMargin,
                    cornerRadius = CornerRadius(130f),
                    color = Color(0xE6FFFFFF),
                )
            }
        }
    )
}

private fun DrawScope.fabButtonClipPath(
    fabButtonSize: Dp,
    navBarTopMargin: Dp,
    fabButtonTopMargin: Dp,
    fabButtonSizeOffset: Dp,
    outerCornerRadius: Float,
    innerCornerRadius: Float,
) = Path().apply {
    val rectSize = Size(
        width = (fabButtonSize + fabButtonSizeOffset * 2).toPx(),
        height = (fabButtonSize + fabButtonSizeOffset * 2).toPx(),
    )
    val rect = Rect(
        offset = Offset(
            x = (size.width - rectSize.width) / 2,
            y = (fabButtonTopMargin - fabButtonSizeOffset).toPx(),
        ),
        size = rectSize
    )

    addRoundRect(
        RoundRect(
            rect = rect,
            bottomLeft = CornerRadius(x = innerCornerRadius, y = innerCornerRadius),
            bottomRight = CornerRadius(x = innerCornerRadius, y = innerCornerRadius),
        )
    )
    val outerCornerDiameter = outerCornerRadius * 2
    val cornerSize = Size(outerCornerDiameter, outerCornerDiameter)
    val cornerXOffset = Offset(outerCornerDiameter, 0f)

    val topLeftCorner = Offset(rect.topLeft.x, navBarTopMargin.toPx())
    val topRightCorner = Offset(rect.topRight.x, navBarTopMargin.toPx())

    moveToOffset(topLeftCorner - cornerXOffset)
    addArc(
        Rect(
            offset = topLeftCorner - cornerXOffset,
            size = cornerSize,
        ),
        startAngleDegrees = 270f,
        sweepAngleDegrees = 90f,
    )
    lineTo(topLeftCorner.x, 0f)

    moveToOffset(topRightCorner)
    addArc(
        Rect(
            offset = topRightCorner,
            size = cornerSize
        ),
        startAngleDegrees = 180f,
        sweepAngleDegrees = 90f,
    )
    lineTo(topRightCorner.x, 0f)
}

private fun DrawScope.drawNavBarMainRectangle(
    topMargin: Dp,
    cornerRadius: CornerRadius,
    color: Color,
) {
    val offset = Offset(x = 0f, y = topMargin.toPx())
    val path = Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    offset = offset,
                    size = Size(size.width - offset.x, size.height - offset.y),
                ),
                topLeft = cornerRadius,
                topRight = cornerRadius,
            )
        )
    }
    drawPath(path = path, color = color)
}

private fun Path.moveToOffset(offset: Offset) = moveTo(offset.x, offset.y)

@Composable
private fun BottomNavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        val color = if (isSelected) {
            MaterialTheme.colorScheme.secondary
        } else {
            MaterialTheme.colorScheme.onSurface
        }

        Image(
            painter = painterResource(id = item.iconId),
            contentDescription = stringResource(item.labelId),
            colorFilter = ColorFilter.tint(color),
            modifier = Modifier.size(32.dp)
        )

        Text(
            text = stringResource(item.labelId),
            style = MaterialTheme.typography.bodySmall,
            color = color,
            autoSize = TextAutoSize.StepBased(
                MaterialTheme.typography.bodySmall.fontSize,
                MaterialTheme.typography.bodySmall.fontSize
            ),
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun BottomNavBarPreview() {
    CalorAiTheme {
        BottomNavBar(
            selectedItem = BottomNavItem.Home,
            onItemSelected = {},
            onFabClick = {}
        )
    }
}
