package dev.calorai.mobile.features.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.LabeledTextField
import dev.calorai.mobile.core.uikit.PrimaryButton
import dev.calorai.mobile.core.uikit.SimpleDropdown
import dev.calorai.mobile.core.utils.ObserveAsEvents
import dev.calorai.mobile.features.main.ui.MainUiAction
import dev.calorai.mobile.features.profile.ui.model.ActivityUi
import dev.calorai.mobile.features.profile.ui.model.GenderUi
import dev.calorai.mobile.features.profile.ui.model.GoalUi
import dev.calorai.mobile.features.profile.ui.model.SavingErrorType
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfileRoot(
    viewModel: ProfileViewModel = koinViewModel(),
    mainUiActions: SharedFlow<MainUiAction>,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.uiActions) { action ->
        coroutineScope.launch {
            when (action) {
                is ProfileUiAction.ShowErrorMessage -> {
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

                ProfileUiAction.ShowSavingMessage -> {
                    snackbarHostState.showSnackbar(context.getString(R.string.settings_profile_saved))
                }

                ProfileUiAction.ShowNetworkError -> snackbarHostState.showSnackbar(
                    context.getString(
                        R.string.settings_load_error
                    )
                )
            }
        }
    }

    ProfileScreen(
        state = uiState,
        hostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    state: ProfileUiState,
    hostState: SnackbarHostState,
    onEvent: (ProfileUiEvent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.typography.titleLarge.color,
                ),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.settings_title))
                        PrimaryButton(
                            onClick = { onEvent(ProfileUiEvent.LogoutClick) },
                            modifier = Modifier.wrapContentWidth(),
                            text = "Выйти"
                        )
                    }
                }
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
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = { onEvent(ProfileUiEvent.OnRefresh) },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding())
            ) {
                ProfileContent(
                    state = state,
                    onEvent = onEvent,
                    isSaving = state.isSaving,
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onEvent: (ProfileUiEvent) -> Unit,
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
            onValueChange = { onEvent(ProfileUiEvent.NameChange(it)) },
            label = stringResource(R.string.settings_name),
            modifier = Modifier.fillMaxWidth(),
        )
        LabeledTextField(
            value = state.user.email,
            onValueChange = { onEvent(ProfileUiEvent.EmailChange(it)) },
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
                onSelected = { onEvent(ProfileUiEvent.GenderChange(it)) },
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
                onSelected = { onEvent(ProfileUiEvent.HeightChange(it)) },
                modifier = Modifier.weight(1f)
            )
            SimpleDropdown(
                label = stringResource(R.string.settings_weight),
                placeholder = stringResource(R.string.settings_weight_val),
                options = weightOptions,
                selected = state.user.weight,
                mapToString = { resources.getString(R.string.settings_weight_val_type, it) },
                onSelected = { onEvent(ProfileUiEvent.WeightChange(it)) },
                modifier = Modifier.weight(1f)
            )
        }
        SimpleDropdown(
            label = stringResource(R.string.settings_activity),
            placeholder = stringResource(R.string.settings_activity),
            options = ActivityUi.entries,
            selected = state.user.activity,
            mapToString = { resources.getString(it.labelResId) },
            onSelected = { onEvent(ProfileUiEvent.ActivityChange(it)) }
        )
        SimpleDropdown(
            label = stringResource(R.string.settings_activity_goal),
            placeholder = stringResource(R.string.settings_activity_goal),
            options = GoalUi.entries,
            selected = state.user.goal,
            mapToString = { resources.getString(it.labelResId) },
            onSelected = { onEvent(ProfileUiEvent.GoalChange(it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { onEvent(ProfileUiEvent.SaveButtonClick) },
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
    onEvent: (ProfileUiEvent) -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = { setShowState(false) },
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onEvent(
                            ProfileUiEvent.BirthDateChange(
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

@Preview(showBackground = true)
@Composable
private fun SettingScreenPreview() {
    CalorAiTheme {
        ProfileScreen(
            state = ProfileUiState(),
            hostState = remember { SnackbarHostState() },
            onEvent = {}
        )
    }
}
