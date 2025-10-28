package dev.calorai.mobile.main.features.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.calorai.mobile.ui.theme.CalorAiTheme

@Composable
fun SettingsRoot() {
    SettingsScreen()
}

@Composable
private fun SettingsScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "SettingsScreen")
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    CalorAiTheme {
        SettingsScreen()
    }
}
