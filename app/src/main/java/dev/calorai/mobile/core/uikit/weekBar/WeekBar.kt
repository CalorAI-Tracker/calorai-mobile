package dev.calorai.mobile.core.uikit.weekBar

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.utils.locale
import java.time.DayOfWeek
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
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        weekData.daysList.forEach { dateUiModel ->
            DayItem(
                dateData = dateUiModel,
                onClick = onDateSelected,
                modifier = Modifier,
                isSelected = dateUiModel.date == weekData.selectedDate,
            )
        }
    }
}

@Composable
private fun DayItem(
    dateData: DateUiModel,
    isSelected: Boolean,
    onClick: (DateUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor = when (dateData.timePeriod) {
        TimePeriod.FUTURE -> MaterialTheme.colorScheme.onSurface
        TimePeriod.PAST,
        TimePeriod.PRESENT,
            -> MaterialTheme.colorScheme.onPrimary
    }
    val progressArcColor = MaterialTheme.colorScheme.onSurface
    val progressArc: Float by animateFloatAsState(
        if (!(isSelected || dateData.timePeriod == TimePeriod.FUTURE)) dateData.progress else 0f
    )
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .width(39.dp)
            .wrapContentHeight()
            .clip(RoundedCornerShape(30.dp))
            .then(
                if (isSelected) {
                    Modifier.border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(30.dp)
                    )
                } else Modifier
            )
            .clickable(enabled = dateData.timePeriod != TimePeriod.FUTURE) {
                onClick(dateData)
            }
            .padding(vertical = 10.dp)

    ) {
        Text(
            text = dateData.date.dayOfWeek.getDisplayName(
                TextStyle.SHORT_STANDALONE,
                LocalContext.current.locale
            ),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            modifier = Modifier
                .drawBehind {
                    drawProgressCircle(
                        progress = progressArc,
                        arcColor = progressArcColor,
                    )
                }
                .width(30.dp)
                .padding(2.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = dateData.date.dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = textColor
        )
    }
}

private fun DrawScope.drawProgressCircle(progress: Float, arcColor: Color) {
    if (progress == 0f) return
    val canvasSize = size
    val center = Offset(canvasSize.width / 2f, canvasSize.height / 2f)
    val outerRadius = maxOf(canvasSize.width, canvasSize.height) / 2f
    val strokeWidth = outerRadius * 0.07f
    val arcRect = Rect(
        left = center.x - outerRadius + strokeWidth / 2,
        top = center.y - outerRadius + strokeWidth / 2,
        right = center.x + outerRadius - strokeWidth / 2,
        bottom = center.y + outerRadius - strokeWidth / 2
    )
    val startAngle = -90f
    val filledArea = progress.coerceIn(0f, 1f)
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

@Preview(showBackground = true, name = "Выбранный день")
@Composable
private fun SelectedDayItemPreview() {
    val context = LocalContext.current
    val date = LocalDate.of(2025, 11, 10)
    CalorAiTheme {
        DayItem(
            dateData = DateUiModel(
                date = date,
                timePeriod = TimePeriod.PRESENT,
                progress = 0.7f,
            ),
            onClick = {},
            isSelected = true,
        )
    }
}

@Preview(showBackground = true, name = "Сегодняшний день")
@Composable
private fun TodayItemPreview() {
    val context = LocalContext.current
    val date = LocalDate.now()
    CalorAiTheme {
        DayItem(
            dateData = DateUiModel(
                date = date,
                timePeriod = TimePeriod.PRESENT,
                progress = 0.7f,
            ),
            onClick = {},
            isSelected = false,
        )
    }
}

@Preview(showBackground = true, name = "Предыдущий день")
@Composable
private fun LastDayItemPreview() {
    val context = LocalContext.current
    val date = LocalDate.now().minusDays(1)
    CalorAiTheme {
        DayItem(
            dateData = DateUiModel(
                date = date,
                timePeriod = TimePeriod.PRESENT,
                progress = 0.9f,
            ),
            onClick = {},
            isSelected = false,
        )
    }
}

@Preview(showBackground = true, name = "Будущий день")
@Composable
private fun FutureDayItemPreview() {
    val context = LocalContext.current
    val date = LocalDate.now().plusDays(1)
    CalorAiTheme {
        DayItem(
            dateData = DateUiModel(
                date = date,
                timePeriod = TimePeriod.PRESENT,
                progress = 0.5f,
            ),
            onClick = {},
            isSelected = false,
        )
    }
}

@Preview(showBackground = true, name = "Панель недели")
@Composable
private fun FirstWeekBarPreview() {
    val context = LocalContext.current
    val today = LocalDate.now()
    val locale = Locale.getDefault()
    val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
    val days = (0L..6L).map { offset ->
        val date = startOfWeek.plusDays(offset)
        DateUiModel(
            date = date,
            timePeriod = TimePeriod.PRESENT,
            progress = 0.6f,
        )
    }
    val weekData = WeekBarUiModel(
        daysList = days,
        selectedDate = today,
    )
    CalorAiTheme {
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
    val context = LocalContext.current
    val today = LocalDate.of(2025, 11, 13)
    val locale = Locale.getDefault()
    val firstDayOfWeek = WeekFields.of(locale).firstDayOfWeek
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
    val days = (0L..6L).map { offset ->
        val date = startOfWeek.plusDays(offset)
        DateUiModel(
            date = date,
            timePeriod = TimePeriod.PRESENT,
            progress = 0.6f,
        )
    }
    val weekData = WeekBarUiModel(
        daysList = days,
        selectedDate = today,
    )
    CalorAiTheme {
        WeekBar(
            weekData = weekData,
            modifier = Modifier,
            onDateSelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Панель недели (выбран не сегодняшний день)")
@Composable
private fun SecondWeekBarPreviewRu() {
    val locale = Locale.forLanguageTag("ru")
    val today = LocalDate.of(2025, 9, 13)
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val days = (0L..6L).map { offset ->
        val date = startOfWeek.plusDays(offset)
        DateUiModel(
            date = date,
            timePeriod = TimePeriod.PRESENT,
            progress = 0.6f,
        )
    }
    val weekData = WeekBarUiModel(
        daysList = days,
        selectedDate = today,
    )
    CalorAiTheme {
        WeekBar(
            weekData = weekData,
            modifier = Modifier,
            onDateSelected = {}
        )
    }
}
