package dev.calorai.mobile.main.features.home.ui.views.weekBar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.calorai.mobile.main.features.home.ui.model.DateUiModel
import dev.calorai.mobile.main.features.home.ui.model.DayItemStyle
import dev.calorai.mobile.main.features.home.ui.model.TimePeriod
import dev.calorai.mobile.main.features.home.ui.model.WeekBarUiModel
import dev.calorai.mobile.ui.theme.CalorAiTheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

@Composable
fun WeekBar(
    weekData: WeekBarUiModel,
    modifier: Modifier = Modifier,
    onDateSelected: (date: DateUiModel) -> Unit = {}
) {
    val days = weekData.daysList
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(23.67.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach {dateUiModel ->
            DayItem(
                dateData = dateUiModel,
                onClick = onDateSelected,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun DayItem(
    dateData: DateUiModel,
    onClick: (DateUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val timePeriod = dateData.date.toTimePeriod()
    val dayShortName = dateData.date.shortDayName()
    val configuration = if (dateData.isSelected) DayItemStyle.SELECTED else DayItemStyle.UNSELECTED
    val arcColor = when (timePeriod) {
        TimePeriod.PRESENT -> MaterialTheme.colorScheme.secondary
        TimePeriod.PAST -> MaterialTheme.colorScheme.onSurface
        else -> Color.Transparent
    }
    val textColor = when (timePeriod) {
        TimePeriod.FUTURE -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onPrimary
    }
    Box(
        modifier = modifier
            .width(configuration.width)
            .height(configuration.height)
            .clip(RoundedCornerShape(30.dp))
            .then(
                if (dateData.isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(30.dp)
                    )
                } else Modifier
            )
            .clickable(enabled = timePeriod != TimePeriod.FUTURE){
                onClick(dateData)
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            DayProgressItem(
                dayShortName = dayShortName,
                progressFractions = dateData.progressFractions,
                arcColor = arcColor,
                textColor = textColor,
                isArcShowing = !(dateData.isSelected || timePeriod == TimePeriod.FUTURE),
                itemSize = DayItemStyle.UNSELECTED.width,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dateData.date.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = textColor
            )
        }
    }
}

@Composable
private fun DayProgressItem(
    dayShortName: String,
    progressFractions: List<Float>,
    arcColor: Color,
    textColor: Color,
    isArcShowing: Boolean,
    itemSize: Dp,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .size(itemSize),
        contentAlignment = Alignment.Center
    ) {
        if (isArcShowing) {
            Canvas(modifier = modifier.matchParentSize()) {
                val canvasSize = size
                val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
                val outerRadius = minOf(canvasSize.width, canvasSize.height) / 2f
                val strokeWidth = outerRadius * 0.07f
                val arcRect = Rect(
                    left = center.x - outerRadius + strokeWidth / 2,
                    top = center.y - outerRadius + strokeWidth / 2,
                    right = center.x + outerRadius - strokeWidth / 2,
                    bottom = center.y + outerRadius - strokeWidth / 2
                )
                val startAngle = -90f
                val filledArea = progressFractions[0].coerceIn(0f, 1f)
                val sweep = (filledArea / 1f) * 360f
                drawArc(
                    color = arcColor,
                    startAngle = startAngle + 0.5f,
                    sweepAngle = sweep - 1f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                    topLeft = arcRect.topLeft,
                    size = arcRect.size
                )
            }
        }
        Text(
            text = dayShortName,
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}

fun LocalDate.shortDayName(locale: Locale = Locale.getDefault()): String {
    return this.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
}

fun LocalDate.toTimePeriod(referenceDate: LocalDate = LocalDate.now()): TimePeriod =
    when {
        this.isBefore(referenceDate) -> TimePeriod.PAST
        this.isAfter(referenceDate) -> TimePeriod.FUTURE
        else -> TimePeriod.PRESENT
    }

@Preview(showBackground = true, name = "Выбранный день")
@Composable
private fun SelectedDayItemPreview() {
    CalorAiTheme {
        DayItem(
            dateData = DateUiModel(
                date = LocalDate.of(2025, 11, 10),
                progressFractions = listOf(0.7f, 0.3f),
                isSelected = true
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Сегодняшний день")
@Composable
private fun TodayItemPreview() {
    CalorAiTheme {
        DayItem(
            dateData = DateUiModel(
                date = LocalDate.now(),
                progressFractions = listOf(0.7f, 0.3f),
                isSelected = false
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Предыдущий день")
@Composable
private fun LastDayItemPreview(){
    CalorAiTheme {
        DayItem(
            dateData = DateUiModel(
                date = LocalDate.now().minusDays(4),
                progressFractions = listOf(0.9f, 0.0f),
                isSelected = false
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Будущий день")
@Composable
private fun FutureDayItemPreview(){
    CalorAiTheme {
        DayItem(
            dateData = DateUiModel(
                date = LocalDate.now().plusDays(1),
                progressFractions = listOf(0.5f, 0.0f),
                isSelected = false
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Панель недели")
@Composable
private fun FirstWeekBarPreview() {
    CalorAiTheme {
        val today = LocalDate.now()
        val locale = Locale.getDefault()
        val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
        val days = (0L..6L).map { offset ->
            val date = startOfWeek.plusDays(offset)
            DateUiModel(
                date = date,
                progressFractions = listOf(0.6f),
                isSelected = date == today
            )
        }
        val weekData = WeekBarUiModel(daysList = days)
        WeekBar(
            weekData = weekData,
            modifier = Modifier,
            onDateSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Панель недели (выбран не сегодняшний день)")
@Composable
private fun SecondWeekBarPreview() {
    CalorAiTheme {
        val today = LocalDate.of(2025, 11, 13)
        val locale = Locale.getDefault()
        val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
        val days = (0L..6L).map { offset ->
            val date = startOfWeek.plusDays(offset)
            DateUiModel(
                date = date,
                progressFractions = listOf(0.6f),
                isSelected = date == today
            )
        }
        val weekData = WeekBarUiModel(daysList = days)
        WeekBar(
            weekData = weekData,
            modifier = Modifier,
            onDateSelected = {}
        )
    }
}
