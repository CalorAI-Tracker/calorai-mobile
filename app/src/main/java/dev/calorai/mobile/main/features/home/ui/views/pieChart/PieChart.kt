package dev.calorai.mobile.main.features.home.ui.views.pieChart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import dev.calorai.mobile.main.features.home.ui.views.utils.angleCorrectionCalc
import androidx.compose.ui.graphics.Paint as ComposePaint
import dev.calorai.mobile.main.features.home.ui.model.PieChartUiModel
import dev.calorai.mobile.ui.theme.CalorAiTheme

@Composable
fun PieChart(
    pieChartData: PieChartUiModel,
    modifier: Modifier = Modifier,
    size: Size
) {
    Box(
        modifier = modifier.size(size.chartSize),
        contentAlignment = Alignment.Center
    ) {
        PieChartCircle(
            pieChartData = pieChartData,
            chartSize = size.chartSize,
            innerRatio = size.innerRatio,
            additionalOffsetCoeff = size.additionalOffsetCoeff,
            colors = listOf(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.matchParentSize()
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = pieChartData.targetText,
                style = size.labelStyle(),
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = pieChartData.targetSubtext,
                style = size.textStyle(),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PieChartCircle(
    pieChartData: PieChartUiModel,
    chartSize: Dp,
    innerRatio: Float,
    additionalOffsetCoeff: Float,
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val values = pieChartData.pieData
    Canvas(modifier = modifier.size(chartSize)) {
        val canvasSize = size
        val total = values.sum()
        val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
        val outerRadius = canvasSize.minDimension / 2f
        val innerRadius = outerRadius * innerRatio
        val strokeWidth = outerRadius - innerRadius
        val arcRect = Rect(
            left = center.x - outerRadius + strokeWidth / 2,
            top = center.y - outerRadius + strokeWidth / 2,
            right = center.x + outerRadius - strokeWidth / 2,
            bottom = center.y + outerRadius - strokeWidth / 2,
        )
        val layerPaint = ComposePaint()
        drawContext.canvas.saveLayer(
            bounds = Rect(
                left = 0f,
                top = 0f,
                right = canvasSize.width,
                bottom = canvasSize.height
            ),
            layerPaint
        )
        var startAngle = -90f
        values.forEachIndexed { index, value ->
            val sweepAngle = (value / total) * 360f
            val angleCorrection = angleCorrectionCalc(
                strokeWidth = strokeWidth,
                outerRadius = outerRadius,
                additionalOffsetCoeff = additionalOffsetCoeff
            )
            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle + angleCorrection,
                sweepAngle = sweepAngle - 2 * angleCorrection,
                useCenter = false,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                topLeft = arcRect.topLeft,
                size = arcRect.size,
            )
            startAngle += sweepAngle
        }
        val clearPaint = ComposePaint().apply { blendMode = BlendMode.Clear }
        drawContext.canvas.drawCircle(center, innerRadius, clearPaint)
        drawContext.canvas.restore()
    }
}

enum class Size(
    val chartSize: Dp,
    val innerRatio: Float,
    val additionalOffsetCoeff: Float,
    val labelStyle: @Composable () -> TextStyle,
    val textStyle: @Composable () -> TextStyle
) {
    Large(
        chartSize = 277.dp,
        innerRatio= 0.65f,
        additionalOffsetCoeff = 1.3f,
        labelStyle = {typography.displayLarge},
        textStyle = {typography.bodyLarge}
    ),
    Medium(
        chartSize = 117.dp,
        innerRatio= 0.736f,
        additionalOffsetCoeff = 1.25f,
        labelStyle = {typography.bodyLarge},
        textStyle = {typography.bodySmall}
    ),
}

@Preview(showBackground = true)
@Composable
fun MediumPieChartPreview() {
    val model = PieChartUiModel(
        targetText = "59 г",
        targetSubtext = "белка\nосталось",
        leftText = "325 ккал",
        pieData = listOf(50f,50f)
    )
    CalorAiTheme {
        PieChart(
            pieChartData = model,
            modifier = Modifier,
            size = Size.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LargePieChartPreview() {
    val model = PieChartUiModel(
        targetText = "1003",
        targetSubtext = "ккал осталось",
        leftText = "325 ккал",
        pieData = listOf(40f, 60f)
    )
    CalorAiTheme {
        PieChart(
            pieChartData = model,
            modifier = Modifier,
            size = Size.Large
        )
    }
}