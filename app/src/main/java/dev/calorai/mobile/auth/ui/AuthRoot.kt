package dev.calorai.mobile.auth.ui

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
import dev.calorai.mobile.ui.theme.CalorAiTheme

@Composable
fun AuthRoot(
    navigateToAuthorizedZone: () -> Unit,
) {
    AuthScreen(
        onClick = navigateToAuthorizedZone,
    )
}

@Composable
private fun AuthScreen(
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "AuthScreen")
        Spacer(modifier = Modifier.size(32.dp))
        Button(onClick) {
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
