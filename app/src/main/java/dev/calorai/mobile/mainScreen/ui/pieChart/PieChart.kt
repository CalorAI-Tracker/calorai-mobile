package dev.calorai.mobile.mainScreen.ui.pieChart

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Paint as ComposePaint
import dev.calorai.mobile.mainScreen.ui.model.PieChartUiModel
import dev.calorai.mobile.mainScreen.ui.utils.angleCorrectionCalc
import dev.calorai.mobile.ui.theme.CalorAiTheme

@Composable
fun PieChart(
    pieChartData: PieChartUiModel,
    modifier: Modifier = Modifier,
    isMainChart: Boolean
) {
    val chartSize = if(isMainChart) 277.dp else 117.dp
    val innerRatio = if (isMainChart) 0.65f else 0.736f
    val additionalOffsetCoeff = if (isMainChart) 1.3f else 1.25f
    Box(
        modifier = modifier.size(chartSize),
        contentAlignment = Alignment.Center
    ) {
        PieChartBackground(
            pieChartData = pieChartData,
            modifier = Modifier.matchParentSize(),
            chartSize = chartSize,
            innerRatio = innerRatio,
            additionalOffsetCoeff = additionalOffsetCoeff
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            Text(
                text = pieChartData.targetText,
                style = if (isMainChart) typography.displayLarge else typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                text = pieChartData.targetSubtext,
                style = if (isMainChart) typography.bodyLarge else typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PieChartBackground(
    pieChartData: PieChartUiModel,
    modifier: Modifier = Modifier,
    chartSize: Dp,
    innerRatio: Float,
    additionalOffsetCoeff: Float
) {
    val values = pieChartData.pieData
    val colors = listOf(
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.primary
    )
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
            bottom = center.y + outerRadius - strokeWidth / 2
        )
        val layerBounds = Rect(0f, 0f, canvasSize.width, canvasSize.height)
        val layerPaint = ComposePaint()
        drawContext.canvas.saveLayer(layerBounds, layerPaint)
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
                size = arcRect.size
            )
            startAngle += sweepAngle
        }
        val clearPaint = ComposePaint().apply { blendMode = BlendMode.Clear }
        drawContext.canvas.drawCircle(center, innerRadius, clearPaint)
        drawContext.canvas.restore()
    }
}

@Preview(showBackground = true)
@Composable
fun PieChartPreview() {
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
            isMainChart = false
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PieChartRoundedPreview() {
    val model = PieChartUiModel(
        targetText = "1003",
        targetSubtext = "ккал осталось",
        leftText = "325 ккал",
        pieData = listOf(20f, 80f)
    )
    CalorAiTheme {
        PieChart(
            pieChartData = model,
            modifier = Modifier,
            isMainChart = true
        )
    }
}