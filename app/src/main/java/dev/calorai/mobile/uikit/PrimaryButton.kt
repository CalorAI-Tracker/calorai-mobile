package dev.calorai.mobile.uikit

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.calorai.mobile.ui.theme.CalorAiTheme


@Composable
fun PrimaryButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        modifier = modifier,
        shape = RoundedCornerShape(25)
    ) {
        content()
    }
}



@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
    CalorAiTheme {
        PrimaryButton(onClick = {}) {}
    }
}