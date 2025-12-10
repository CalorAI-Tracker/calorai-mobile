package dev.calorai.mobile.features.meal.create.ui

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
import dev.calorai.mobile.core.uikit.PrimaryTextField
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

        ManualTextFieldWithTitle("12",{},"GGGG")

        PrimaryButton(
            onClick = { onEvent(CreateMealManualUiEvent.AddClick) },
            text = "Добавить",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun ManualTextFieldWithTitle(
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    modifier: Modifier = Modifier
) {
    Column {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(10.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = modifier
                .shadow(1.dp, RoundedCornerShape(50)),
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                errorContainerColor = MaterialTheme.colorScheme.surface,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "Edit",
                    tint = MaterialTheme.colorScheme.onSurface,
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
