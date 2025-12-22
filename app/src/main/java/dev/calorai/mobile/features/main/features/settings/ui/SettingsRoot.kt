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
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.utils.ObserveAsEvents
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.calorai.mobile.features.main.features.settings.ui.model.ActivityUi
import dev.calorai.mobile.features.main.features.settings.ui.model.GenderUi
import dev.calorai.mobile.features.main.features.settings.ui.model.GoalUi
import dev.calorai.mobile.features.main.features.settings.ui.model.SavingErrorType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoot(
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()


    ObserveAsEvents(viewModel.uiActions) { action ->
        coroutineScope.launch {
            when (action) {
                is SettingsUiAction.ShowErrorMessage -> {
                    val message = when (action.errorType) {
                        SavingErrorType.NUMBER_PARSE -> context.getString(
                            R.string.settings_number_parse_error,
                            action.value ?: ""
                        )

                        SavingErrorType.UNKNOWN_GENDER -> context.getString(
                            R.string.settings_unknown_gender,
                            action.value ?: ""
                        )

                        SavingErrorType.UNKNOWN_ACTIVITY -> context.getString(
                            R.string.settings_unknown_activity,
                            action.value ?: ""
                        )

                        SavingErrorType.UNKNOWN_GOAL -> context.getString(
                            R.string.settings_unknown_goal,
                            action.value ?: ""
                        )

                        SavingErrorType.SAVE_ERROR -> context.getString(R.string.settings_save_error)
                    }
                    snackbarHostState.showSnackbar(message)
                }

                SettingsUiAction.ShowSavingMessage -> {
                    snackbarHostState.showSnackbar(context.getString(R.string.settings_profile_saved))
                }
            }
        }
    }

    SettingsScreen(
        state = uiState,
        hostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    hostState: SnackbarHostState,
    onEvent: (SettingsUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.typography.titleLarge.color,
                ),
                title = { Text(text = stringResource(R.string.settings_title)) }
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = hostState,
                modifier = Modifier.padding(bottom = 32.dp),
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
                state = state,
                onEvent = onEvent,
                isSaving = state.isSaving,
            )
        }
    }
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onEvent: (SettingsUiEvent) -> Unit,
    isSaving: Boolean,
) {
    val scroll = rememberScrollState()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val resources = LocalResources.current
    var heightOptions by remember { mutableStateOf((140..210 step 5).toList()) }
    var weightOptions by remember { mutableStateOf((40..140 step 5).toList()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        LabeledTextField(
            value = state.user.name,
            onValueChange = { onEvent(SettingsUiEvent.NameChange(it)) },
            label = stringResource(R.string.settings_name),
            modifier = Modifier.fillMaxWidth(),
        )
        LabeledTextField(
            value = state.user.email,
            onValueChange = { onEvent(SettingsUiEvent.EmailChange(it)) },
            label = stringResource(R.string.settings_email),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            LabeledTextField(
                value = state.user.birthDate,
                label = stringResource(R.string.settings_birthday),
                readOnly = true,
                onClick = { showDatePicker = true },
                modifier = Modifier.weight(1f)
            )
            SimpleDropdown(
                label = stringResource(R.string.settings_sex),
                placeholder = stringResource(R.string.settings_sex),
                options = GenderUi.entries,
                selected = state.user.gender,
                mapToString = { resources.getString(it.labelResId) },
                onSelected = { onEvent(SettingsUiEvent.GenderChange(it)) },
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SimpleDropdown(
                label = stringResource(R.string.settings_height),
                placeholder = stringResource(R.string.settings_height_val),
                options = heightOptions,
                selected = state.user.height,
                mapToString = { resources.getString(R.string.settings_height_val_type, it) },
                onSelected = { onEvent(SettingsUiEvent.HeightChange(it)) },
                modifier = Modifier.weight(1f)
            )
            SimpleDropdown(
                label = stringResource(R.string.settings_weight),
                placeholder = stringResource(R.string.settings_weight_val),
                options = weightOptions,
                selected = state.user.weight,
                mapToString = { resources.getString(R.string.settings_weight_val_type, it) },
                onSelected = { onEvent(SettingsUiEvent.WeightChange(it)) },
                modifier = Modifier.weight(1f)
            )
        }
        SimpleDropdown(
            label = stringResource(R.string.settings_activity),
            placeholder = stringResource(R.string.settings_activity),
            options = ActivityUi.entries,
            selected = state.user.activity,
            mapToString = { resources.getString(it.labelResId) },
            onSelected = { onEvent(SettingsUiEvent.ActivityChange(it)) }
        )
        SimpleDropdown(
            label = stringResource(R.string.settings_activity_goal),
            placeholder = stringResource(R.string.settings_activity_goal),
            options = GoalUi.entries,
            selected = state.user.goal,
            mapToString = { resources.getString(it.labelResId) },
            onSelected = { onEvent(SettingsUiEvent.GoalChange(it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onEvent(SettingsUiEvent.SaveButtonClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            enabled = !isSaving,
        ) {
            Text(
                text = stringResource(
                    if (state.isSaving) {
                        R.string.settings_saving
                    } else {
                        R.string.settings_save_btn
                    }
                ),
                color = MaterialTheme.colorScheme.surface
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
    if (showDatePicker) {
        BirthDayPicker(
            setShowState = { showDatePicker = it },
            datePickerState = datePickerState,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun BirthDayPicker(
    setShowState: (Boolean) -> Unit,
    datePickerState: DatePickerState,
    onEvent: (SettingsUiEvent) -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = { setShowState(false) },
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onEvent(
                            SettingsUiEvent.BirthDateChange(
                                it
                            )
                        )
                    }
                    setShowState(false)
                }
            ) { Text(stringResource(R.string.settings_date_picker_apply)) }
        }
    ) {
        DatePicker(state = datePickerState)
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
private fun <T> SimpleDropdown(
    label: String,
    placeholder: String,
    options: List<T>,
    selected: T?,
    mapToString: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var stringOptions by remember { mutableStateOf(options.map { it to mapToString(it) }) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier,
    ) {
        Column(modifier = modifier) {
            Text(text = label)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = selected?.let { mapToString(it) } ?: "",
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
            stringOptions.forEach { (option, stringOpt) ->
                DropdownMenuItem(
                    text = { Text(stringOpt) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    CalorAiTheme {
        SettingsScreen(
            state = SettingsUiState(),
            hostState = remember { SnackbarHostState() },
            onEvent = {}
        )
    }
}
