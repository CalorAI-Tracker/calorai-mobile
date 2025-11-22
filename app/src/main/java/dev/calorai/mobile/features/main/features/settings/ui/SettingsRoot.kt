package dev.calorai.mobile.features.main.features.settings.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.calorai.mobile.core.uikit.CalorAiTheme
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun SettingsRoot(
    viewModel: SettingsViewModel = koinViewModel(),
) {
    SettingsScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    onSave: (UserSettingsUiState) -> Unit = {}
) {

    var uiState by remember { mutableStateOf(UserSettingsUiState()) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.typography.titleLarge.color,
                ),
                title = { Text(text = "Настройки") }
            )
        },
        containerColor = Color.Transparent,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            SettingsContent(
                state = uiState,
                onStateChange = { uiState = it },
                onSaveClick = { onSave(uiState) }
            )
        }
    }
}

@Composable
private fun SettingsContent(
    state: UserSettingsUiState,
    onStateChange: (UserSettingsUiState) -> Unit,
    onSaveClick: () -> Unit
) {
    val scroll = rememberScrollState()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        LabeledTextField(
            value = state.name,
            onValueChange = { onStateChange(state.copy(name = it)) },
            label = "Имя",
            modifier = Modifier.fillMaxWidth(),
        )
        LabeledTextField(
            value = state.email,
            onValueChange = { onStateChange(state.copy(email = it)) },
            label = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            LabeledTextField(
                value = state.birthDate,
                label = "Дата рождения",
                readOnly = true,
                onClick = { showDatePicker = true },
                modifier = Modifier.weight(1f)
            )
            SimpleDropdown(
                label = "Пол",
                placeholder = "Пол",
                options = listOf("Женский", "Мужской"),
                selected = state.gender,
                onSelected = { onStateChange(state.copy(gender = it)) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SimpleDropdown(
                label = "Рост",
                placeholder = "Рост (см)",
                options = (140..210 step 5).map { "$it см" },
                selected = state.height,
                onSelected = { onStateChange(state.copy(height = it)) },
                modifier = Modifier.weight(1f)
            )
            SimpleDropdown(
                label = "Вес",
                placeholder = "Вес (кг)",
                options = (40..140 step 5).map { "$it кг" },
                selected = state.weight,
                onSelected = { onStateChange(state.copy(weight = it)) },
                modifier = Modifier.weight(1f)
            )
        }
        SimpleDropdown(
            label = "Активность",
            placeholder = "Активность",
            options = listOf(
                "0–2 (Иногда тренируюсь)",
                "3–4 (Умеренная активность)",
                "5–6 (Высокая активность)",
                "7+ (Профессионально)"
            ),
            selected = state.activity,
            onSelected = { onStateChange(state.copy(activity = it)) }
        )
        SimpleDropdown(
            label = "Цель",
            placeholder = "Цель",
            options = listOf("Сбросить вес", "Поддерживать вес", "Набрать массу"),
            selected = state.goal,
            onSelected = { onStateChange(state.copy(goal = it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Сохранить", color = MaterialTheme.colorScheme.surface)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val instant = Instant.ofEpochMilli(it)
                            val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                            onStateChange(
                                state.copy(
                                    birthDate = localDate.format(
                                        DateTimeFormatter.ofPattern(
                                            "dd MMMM yyyy"
                                        )
                                    )
                                )
                            )
                        }
                        showDatePicker = false
                    }
                ) { Text("Apply") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String,
    readOnly: Boolean = false,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onClick: () -> Unit = {},
) {
    Column(modifier = modifier) {
        Text(text = label)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(label) },
            modifier = Modifier
                .fillMaxWidth()
                .shadow(3.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = keyboardOptions,
            readOnly = readOnly,
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            onClick.invoke()
                        }
                    }
                }
            },
            maxLines = 1,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SimpleDropdown(
    label: String,
    placeholder: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        Column(modifier = modifier) {
            Text(text = label)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = selected,
                onValueChange = { /* readOnly */ },
                readOnly = true,
                placeholder = { Text(placeholder) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
                    .shadow(3.dp, RoundedCornerShape(16.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    errorContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                maxLines = 1,
            )
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { opt ->
                DropdownMenuItem(
                    text = { Text(opt) },
                    onClick = {
                        onSelected(opt)
                        expanded = false
                    }
                )
            }
        }
    }
}

data class UserSettingsUiState(
    val name: String = "",
    val email: String = "",
    val birthDate: String = "",
    val gender: String = "",
    val height: String = "",
    val weight: String = "",
    val activity: String = "",
    val goal: String = ""
)

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    CalorAiTheme {
        SettingsScreen()
    }
}
