package dev.calorai.mobile.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.calorai.mobile.R
import dev.calorai.mobile.ui.theme.CalorAiTheme
import dev.calorai.mobile.ui.theme.Pink
import dev.calorai.mobile.ui.theme.White

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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Pink, White)))
            .padding(8.dp, 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TitleWithDescription(
            title = stringResource(R.string.auth_label_greeting1),
            description = stringResource(R.string.auth_label_greeting2)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = stringResource(R.string.auth_label_email),
                style = MaterialTheme.typography.bodyLarge
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.auth_placeholder_email),
                        color = Color.Gray
                    ) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp)),
                shape = MaterialTheme.shapes.medium // Форма поля
            )
            Text(
                text = stringResource(R.string.auth_label_password),
                style = MaterialTheme.typography.bodyLarge
            )
            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = {
                    Text(
                        text = stringResource(R.string.auth_placeholder_email),
                        color = Color.Gray
                    ) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp)),
                shape = MaterialTheme.shapes.medium // Форма поля
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Black
                )

                Box(
                    modifier = Modifier
                        .background(Color.Black)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = stringResource(R.string.auth_divider_or),
                        color = Color.White,
                        fontSize = 12.sp
                    )
                }

                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color.Black
                )
            }

            OutlinedButton(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Pink, shape = MaterialTheme.shapes.medium)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_search), // TODO Заменить на иконку гугла
                    contentDescription = "Google",
                    modifier = Modifier.size(24.dp)
                )
                Text("Continue with Google", color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.size(32.dp))
        Button(onClick) {
            Text("Authorize")
        }
    }
}

@Composable
fun TitleWithDescription(
    title: String,
    description: String,
) {
    Column (
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 5.dp),
            //fontSize = 28.sp,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun AuthScreenPreview() {
    CalorAiTheme {
        AuthScreen()
    }
}
