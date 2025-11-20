package dev.calorai.mobile.features.main.features.settings.ui


import androidx.compose.foundation.background
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.calorai.mobile.core.uikit.CalorAiTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoot(
    viewModel: SettingsViewModel = koinViewModel(),
) {
    SettingsScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSave: (UserSettingsUiState) -> Unit = {}
) {

    var uiState by remember { mutableStateOf(UserSettingsUiState()) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(text = "Настройки") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text ("Имя")
        LabeledTextField(
            value = state.name,
            onValueChange = { onStateChange(state.copy(name = it)) },
            label = "Имя"
        )

        Text ( "Email")
        LabeledTextField(
            value = state.email,
            onValueChange = { onStateChange(state.copy(email = it)) },
            label = "Email",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        Text ( "Дата рождения, пол")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            LabeledTextField(
                value = state.birthDate,
                onValueChange = { onStateChange(state.copy(birthDate = it)) },
                label = "Дата рождения",
                modifier = Modifier.weight(1f)
            )
            SimpleDropdown(
                label = "Пол",
                options = listOf("Женский", "Мужской"),
                selected = state.gender,
                onSelected = { onStateChange(state.copy(gender = it)) },
                modifier = Modifier.weight(1f)
            )
        }

        Text ( "Рост и вес")
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            SimpleDropdown(
                label = "Рост (см)",
                options = (140..210 step 5).map { "$it см" },
                selected = state.height,
                onSelected = { onStateChange(state.copy(height = it)) },
                modifier = Modifier.weight(1f)
            )
            SimpleDropdown(
                label = "Вес (кг)",
                options = (40..140 step 5).map { "$it кг" },
                selected = state.weight,
                onSelected = { onStateChange(state.copy(weight = it)) },
                modifier = Modifier.weight(1f)
            )
        }

        Text ( "Активность")
        SimpleDropdown(
            label = "Активность",
            options = listOf(
                "0–2 (Иногда тренируюсь)",
                "3–4 (Умеренная активность)",
                "5–6 (Высокая активность)",
                "7+ (Профессионально)"
            ),
            selected = state.activity,
            onSelected = { onStateChange(state.copy(activity = it)) }
        )

        Text ( "Цель")
        SimpleDropdown(
            label = "Цель",
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
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(text = "Сохранить", color = MaterialTheme.colorScheme.surface)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun LabeledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        keyboardOptions = keyboardOptions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDropdown(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = { /* readOnly */ },
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

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
fun SettingScreenPreview() {
    CalorAiTheme {
        SettingsScreen()
    }
}
