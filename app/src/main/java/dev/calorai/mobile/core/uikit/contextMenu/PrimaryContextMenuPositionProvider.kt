package dev.calorai.mobile.core.uikit.contextMenu

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider
import kotlin.math.roundToInt

class PrimaryContextMenuPositionProvider(
    private val anchorOffset: Offset,
) : PopupPositionProvider {

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val desiredX = anchorOffset.x.roundToInt()
        val desiredY = anchorOffset.y.roundToInt() + popupContentSize.height - 32

        val maxX = windowSize.width - popupContentSize.width
        val maxY = windowSize.height - popupContentSize.height

        val finalX = desiredX.coerceIn(0, maxX.coerceAtLeast(0))
        val finalY = desiredY.coerceIn(0, maxY.coerceAtLeast(0))

        return IntOffset(finalX, finalY)
    }
}