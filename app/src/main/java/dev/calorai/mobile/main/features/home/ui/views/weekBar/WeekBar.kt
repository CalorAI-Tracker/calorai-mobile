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
import dev.calorai.mobile.main.features.home.ui.model.WeekDay
import dev.calorai.mobile.ui.theme.CalorAiTheme
import java.util.Calendar

@Composable
fun WeekBar(
    weekData: WeekBarUiModel,
    modifier: Modifier = Modifier,
    onDateSelected: (date: DateUiModel) -> Unit = {}
) {
    val days = weekData.daysList
    var selectedIndex = weekData.selectedDay
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(23.67.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEachIndexed { index, dateUiModel ->
            val isSelected = (index == selectedIndex)
            DayItem(
                isSelected = isSelected,
                dateData = dateUiModel,
                configuration = if (isSelected) DayItemStyle.SELECTED else DayItemStyle.UNSELECTED,
                onClick = {
                    selectedIndex = index
                    onDateSelected(dateUiModel)
                }
            )
        }
    }
}

@Composable
fun DayItem(
    isSelected: Boolean,
    dateData: DateUiModel,
    configuration: DayItemStyle,
    onClick: (DateUiModel) -> Unit,
    modifier: Modifier = Modifier
) {
    val arcColor = when (dateData.timePeriod) {
        TimePeriod.PRESENT -> MaterialTheme.colorScheme.secondary
        TimePeriod.PAST -> MaterialTheme.colorScheme.onSurface
        else -> Color.Transparent
    }
    val textColor = when {
        dateData.timePeriod == TimePeriod.FUTURE -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onPrimary
    }
    Box(
        modifier = modifier
            .width(configuration.width)
            .height(configuration.height)
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
            .let {
                if (dateData.timePeriod == TimePeriod.FUTURE) it
                else it.clickable(onClick = { onClick(dateData) })
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (isSelected || dateData.timePeriod == TimePeriod.FUTURE) {
                DayProgressItem(
                    dayShortName = dateData.day.shortName,
                    progressFractions = dateData.progressFractions,
                    arcColor = arcColor,
                    textColor = textColor,
                    isArcShowing = false,
                    itemSize = DayItemStyle.UNSELECTED.width,
                    modifier = Modifier
                )
            } else {
                DayProgressItem(
                    dayShortName = dateData.day.shortName,
                    progressFractions = dateData.progressFractions,
                    arcColor = arcColor,
                    textColor = textColor,
                    isArcShowing = true,
                    itemSize = DayItemStyle.UNSELECTED.width,
                    modifier = Modifier
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dateData.date.get(Calendar.DAY_OF_MONTH).toString(),
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
        if(isArcShowing) {
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

@Preview(showBackground = true, name = "Выбранный день")
@Composable
fun SelectedDayItemPreview() {
    CalorAiTheme {
        DayItem(
            isSelected = true,
            dateData = DateUiModel(
                date = Calendar.getInstance().apply { set(2025, Calendar.NOVEMBER, 10) },
                day = WeekDay.MONDAY,
                progressFractions = listOf(0.7f, 0.3f),
                timePeriod = TimePeriod.PRESENT
            ),
            configuration = DayItemStyle.SELECTED,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Сегодняшний день")
@Composable
fun TodayItemPreview() {
    CalorAiTheme {
        DayItem(
            isSelected = false,
            dateData = DateUiModel(
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 0) },
                day = WeekDay.THURSDAY,
                progressFractions = listOf(0.7f, 0.3f),
                timePeriod = TimePeriod.PRESENT
            ),
            configuration = DayItemStyle.UNSELECTED,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Предыдущий день")
@Composable
fun LastDayItemPreview(){
    CalorAiTheme {
        DayItem(
            isSelected = false,
            dateData = DateUiModel(
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -4) },
                day = WeekDay.SUNDAY,
                progressFractions = listOf(0.9f, 0.0f),
                timePeriod = TimePeriod.PAST
            ),
            configuration = DayItemStyle.UNSELECTED,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Будущий день")
@Composable
fun FutureDayItemPreview(){
    CalorAiTheme {
        DayItem(
            isSelected = false,
            dateData = DateUiModel(
                date = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, +1) },
                day = WeekDay.FRIDAY,
                progressFractions = listOf(0.5f, 0.0f),
                timePeriod = TimePeriod.FUTURE
            ),
            configuration = DayItemStyle.UNSELECTED,
            onClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Панель недели")
@Composable
private fun FirstWeekBarPreview() {
    CalorAiTheme {
        val today = Calendar.getInstance().apply { set(2025, Calendar.NOVEMBER, 13) }
        val days = ( -3..3 ).map { offset ->
            val date = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, offset) }
            val timePeriod = when {
                offset == 0 -> TimePeriod.PRESENT
                offset < 0 -> TimePeriod.PAST
                else -> TimePeriod.FUTURE
            }
            DateUiModel(
                date = date,
                day = when (date.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> WeekDay.MONDAY
                    Calendar.TUESDAY -> WeekDay.TUESDAY
                    Calendar.WEDNESDAY -> WeekDay.WEDNESDAY
                    Calendar.THURSDAY -> WeekDay.THURSDAY
                    Calendar.FRIDAY -> WeekDay.FRIDAY
                    Calendar.SATURDAY -> WeekDay.SATURDAY
                    else -> WeekDay.SUNDAY
                },
                progressFractions = listOf(0.6f),
                timePeriod = timePeriod
            )
        }
        val weekData = WeekBarUiModel(
            selectedDay = 3,
            daysList = days
        )
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
        val today = Calendar.getInstance().apply { set(2025, Calendar.NOVEMBER, 13) }
        val days = ( -3..3 ).map { offset ->
            val date = (today.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, offset) }
            val timePeriod = when {
                offset == 0 -> TimePeriod.PRESENT
                offset < 0 -> TimePeriod.PAST
                else -> TimePeriod.FUTURE
            }
            DateUiModel(
                date = date,
                day = when (date.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> WeekDay.MONDAY
                    Calendar.TUESDAY -> WeekDay.TUESDAY
                    Calendar.WEDNESDAY -> WeekDay.WEDNESDAY
                    Calendar.THURSDAY -> WeekDay.THURSDAY
                    Calendar.FRIDAY -> WeekDay.FRIDAY
                    Calendar.SATURDAY -> WeekDay.SATURDAY
                    else -> WeekDay.SUNDAY
                },
                progressFractions = listOf(0.6f),
                timePeriod = timePeriod
            )
        }
        val weekData = WeekBarUiModel(
            selectedDay = 2,
            daysList = days
        )
        WeekBar(
            weekData = weekData,
            modifier = Modifier,
            onDateSelected = {}
        )
    }
}