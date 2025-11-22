package dev.calorai.mobile.features.auth.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.calorai.mobile.core.uikit.CalorAiTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthRoot(
    viewModel: AuthViewModel = koinViewModel(),
) {
    AuthScreen(
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun AuthScreen(
    onEvent: (AuthUiEvent) -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "AuthScreen")
        Spacer(modifier = Modifier.size(32.dp))
        Button(onClick = { onEvent(AuthUiEvent.ButtonClick)}) {
            Text("Authorize")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
    CalorAiTheme {
        AuthScreen()
    }
}
