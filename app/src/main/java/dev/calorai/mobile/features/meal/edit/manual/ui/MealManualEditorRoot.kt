package dev.calorai.mobile.features.meal.edit.manual.ui

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.PrimaryButton
import dev.calorai.mobile.core.uikit.PrimaryTextField
import dev.calorai.mobile.core.uikit.PrimaryTextFieldWithTitle
import dev.calorai.mobile.core.uikit.commonGradientBackground
import dev.calorai.mobile.features.meal.domain.model.MealType
import org.koin.androidx.compose.koinViewModel

@Composable
fun MealManualEditorRoot(
    viewModel: MealManualEditorViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler { viewModel.onEvent(MealManualEditorUiEvent.BackPressed) }
    MealManualEditorScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun MealManualEditorScreen(
    uiState: MealManualEditorUiState,
    onEvent: (MealManualEditorUiEvent) -> Unit,
) {
    val system = WindowInsets.systemBars.asPaddingValues()
    val title = stringResource(uiState.titleTextRes)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .commonGradientBackground()
            .padding(
                top = system.calculateTopPadding() + 32.dp,
                bottom = system.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp,
            ),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.size(5.dp))
        when (uiState) {
            is MealManualEditorUiState.Loading -> Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }

            is MealManualEditorUiState.Ready -> MealManualEditorScreenReady(uiState, onEvent)
        }
    }
}

@Composable
private fun ColumnScope.MealManualEditorScreenReady(
    uiState: MealManualEditorUiState.Ready,
    onEvent: (MealManualEditorUiEvent) -> Unit,
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            onEvent(MealManualEditorUiEvent.PickImage(uri))
        }
    )

    Text(
        text = stringResource(R.string.create_meal_manual_description),
        style = MaterialTheme.typography.bodyMedium,
    )
    Spacer(modifier = Modifier.size(20.dp))
    Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProductNameField(
                value = uiState.name,
                onValueChange = { onEvent(MealManualEditorUiEvent.NameChange(it)) },
                modifier = Modifier.weight(1f)
            )
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
                            maxItems = 1
                        )
                    )
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_gallery),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.surface,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ManualTextFieldWithTitle(
                title = stringResource(R.string.create_meal_manual_protein),
                value = uiState.proteins,
                onValueChange = { onEvent(MealManualEditorUiEvent.ProteinsChange(it)) },
                modifier = Modifier.weight(1f)
            )
            ManualTextFieldWithTitle(
                title = stringResource(R.string.create_meal_manual_fats),
                value = uiState.fats,
                onValueChange = { onEvent(MealManualEditorUiEvent.FatsChange(it)) },
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ManualTextFieldWithTitle(
                title = stringResource(R.string.create_meal_manual_carbohydrates),
                value = uiState.carbs,
                onValueChange = { onEvent(MealManualEditorUiEvent.CarbsChange(it)) },
                modifier = Modifier.weight(1f)
            )
            ManualTextFieldWithTitle(
                title = stringResource(R.string.create_meal_manual_portion),
                value = uiState.portion,
                onValueChange = { onEvent(MealManualEditorUiEvent.PortionChange(it)) },
                modifier = Modifier.weight(1f)
            )
        }
    }

    Spacer(Modifier.weight(1f))

    PrimaryButton(
        onClick = { onEvent(MealManualEditorUiEvent.SubmitClick) },
        text = stringResource(uiState.actionButtonTextRes),
    )
    Spacer(Modifier.size(32.dp))
}

@Composable
private fun ProductNameField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    PrimaryTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = stringResource(R.string.create_meal_manual_name_placeholder),
        modifier = modifier,
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        )
    )
}

@Composable
private fun ManualTextFieldWithTitle(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    focusManager: FocusManager = LocalFocusManager.current
) {
    PrimaryTextFieldWithTitle(
        title = title,
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = stringResource(R.string.create_meal_manual_textfield_placeholder),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next,
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                onValueChange(formatDoubleInput(value))
                focusManager.moveFocus(FocusDirection.Next)
            }
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CreateMealManualScreenReadyPreview() {
    CalorAiTheme {
        MealManualEditorScreen(
            uiState = MealManualEditorUiState.Ready(
                mode = MealManualEditorMode.Create,
                name = "Овсяная каша",
                proteins = "2.5",
                fats = "1.5",
                carbs = "12.0",
                portion = "100.0",
                mealType = MealType.BREAKFAST,
            ),
            onEvent = {}
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun CreateMealManualScreenLoadingPreview() {
    CalorAiTheme {
        MealManualEditorScreen(
            uiState = MealManualEditorUiState.Loading(
                mode = MealManualEditorMode.Create,
                mealType = MealType.BREAKFAST,
            ),
            onEvent = {}
        )
    }
}

