package dev.calorai.mobile.features.meal.create.manual.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.PrimaryButton
import dev.calorai.mobile.core.uikit.commonGradientBackground
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateMealManualRoot(
    viewModel: CreateMealManualViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateMealManualScreen(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun CreateMealManualScreen(
    uiState: CreateMealManualUiState,
    onEvent: (CreateMealManualUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .commonGradientBackground()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {

            ProductNameField(
                value = uiState.name,
                onValueChange = { onEvent(CreateMealManualUiEvent.NameChange(it)) },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ManualTextFieldWithTitle(
                    title = stringResource(R.string.create_meal_manual_calories),
                    value = uiState.calories.toString(),
                    onValueChange = { onEvent(CreateMealManualUiEvent.CaloriesChange(it)) },
                    modifier = Modifier.weight(1f)
                )
                ManualTextFieldWithTitle(
                    title = stringResource(R.string.create_meal_manual_protein),
                    value = uiState.proteins.toString(),
                    onValueChange = { onEvent(CreateMealManualUiEvent.ProteinsChange(it)) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ManualTextFieldWithTitle(
                    title = stringResource(R.string.create_meal_manual_fats),
                    value = uiState.fats.toString(),
                    onValueChange = { onEvent(CreateMealManualUiEvent.FatsChange(it)) },
                    modifier = Modifier.weight(1f)
                )
                ManualTextFieldWithTitle(
                    title = stringResource(R.string.create_meal_manual_carbohydrates),
                    value = uiState.carbs.toString(),
                    onValueChange = { onEvent(CreateMealManualUiEvent.CarbsChange(it)) },
                    modifier = Modifier.weight(1f)
                )
            }

            ManualTextFieldWithTitle(
                title = stringResource(R.string.create_meal_manual_portion),
                value = uiState.portion.toString(),
                onValueChange = { onEvent(CreateMealManualUiEvent.PortionChange(it)) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        PrimaryButton(
            onClick = { onEvent(CreateMealManualUiEvent.AddClick) },
            text = stringResource(R.string.create_meal_manual_add),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ProductNameField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        placeholder = { Text(text = stringResource(R.string.create_meal_manual_name)) },
        shape = RoundedCornerShape(50),
        textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium),
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(50)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
        ),
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
    placeholder: String = "",
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Number,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )

        Spacer(Modifier.size(10.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = {
                if (placeholder.isNotEmpty()) Text(placeholder)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            shape = RoundedCornerShape(50),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, RoundedCornerShape(50)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
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
}

@Preview(showBackground = true)
@Composable
private fun CreateMealManualScreenPreview() {
    CalorAiTheme {
        CreateMealManualScreen(
            uiState = CreateMealManualUiState(
                name = "Овсяная каша",
                calories = 71.0,
                proteins = 2.5,
                fats = 1.5,
                carbs = 12.0,
                portion = 100.0
            ),
            onEvent = {}
        )
    }
}
