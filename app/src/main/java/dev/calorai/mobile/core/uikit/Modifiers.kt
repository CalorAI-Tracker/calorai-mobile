package dev.calorai.mobile.core.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.commonGradientBackground(): Modifier =
    this.background(
        Brush.verticalGradient(
            colors = listOf(Pink, White),
        ),
        alpha = 0.3f
    )

fun Modifier.blurShadow(
    color: Color = Color.Black,
    blurRadius: Dp = 20.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 8.dp,
    spread: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(16.dp)
) = this.then(
    Modifier.drawBehind {
        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            setShadowLayer(
                blurRadius.toPx(),
                offsetX.toPx(),
                offsetY.toPx(),
                color.toArgb()
            )
        }

        val outline = shape.createOutline(size, layoutDirection, this)
        drawIntoCanvas { canvas ->
            when (outline) {
                is Outline.Rounded -> {
                    canvas.nativeCanvas.drawRoundRect(
                        outline.roundRect.left - spread.toPx(),
                        outline.roundRect.top - spread.toPx(),
                        outline.roundRect.right + spread.toPx(),
                        outline.roundRect.bottom + spread.toPx(),
                        outline.roundRect.bottomLeftCornerRadius.x,
                        outline.roundRect.bottomLeftCornerRadius.y,
                        paint
                    )
                }
                else -> {}
            }
        }
    }
)
