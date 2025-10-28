package dev.calorai.mobile.main.features.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.calorai.mobile.ui.theme.CalorAiTheme

@Composable
fun HomeRoot() {
    HomeScreen()
}

@Composable
private fun HomeScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "HomeScreen")
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    CalorAiTheme {
        HomeScreen()
    }
}
