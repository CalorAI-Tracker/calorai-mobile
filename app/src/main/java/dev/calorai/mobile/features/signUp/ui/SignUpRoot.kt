package dev.calorai.mobile.features.signUp.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.PrimaryButton
import dev.calorai.mobile.core.uikit.PrimaryTextField
import dev.calorai.mobile.core.uikit.commonGradientBackground
import org.koin.androidx.compose.koinViewModel


@Composable
fun SignUpRoot(
    viewModel: SignUpViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SignUpScreen(
        state = state,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun SignUpScreen(
    state: SignUpUiState,
    onEvent: (SignUpUiEvent) -> Unit = {}
) {
    val system = WindowInsets.systemBars.asPaddingValues()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .commonGradientBackground()
            .padding(
                top = system.calculateTopPadding(),
                bottom = system.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    )  {
        TitleWithDescription(
            title = stringResource(R.string.signup_title),
            description = stringResource(R.string.signup_description)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Spacer(Modifier.size(51.dp))
            TextFieldWithTitle(
                title = stringResource(R.string.signup_name),
                placeholder = stringResource(R.string.signup_placeholder_name),
                value = state.name,
                onValueChange = { onEvent(SignUpUiEvent.NameEntered(it)) },
                keyboardType = KeyboardType.Text
            )
            Spacer(Modifier.size(20.dp))
            TextFieldWithTitle(
                title = stringResource(R.string.auth_label_email),
                placeholder = stringResource(R.string.auth_placeholder_email),
                value = state.email,
                onValueChange = { onEvent(SignUpUiEvent.EmailEntered(it)) },
                keyboardType = KeyboardType.Email
            )
            Spacer(Modifier.size(20.dp))
            TextFieldWithTitle(
                title = stringResource(R.string.auth_label_password),
                placeholder = stringResource(R.string.auth_placeholder_password),
                value = state.password,
                onValueChange = { onEvent(SignUpUiEvent.PasswordEntered(it)) },
                isPassword = true
            )

            Spacer(Modifier.weight(1f))
            PrimaryButton(
                onClick = { onEvent(SignUpUiEvent.SignUpButtonClick) },
                text = stringResource(R.string.auth_continue),
            )
            Spacer(Modifier.size(9.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.signup_account_exist),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.size(3.dp))
                Text(
                    text = stringResource(R.string.signup_login),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.clickable {
                        onEvent(SignUpUiEvent.LoginClick)
                    }
                )
            }
        }
    }
}

@Composable
private fun TitleWithDescription(
    title: String,
    description: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(Modifier.size(5.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun TextFieldWithTitle(
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.size(4.dp))
        PrimaryTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            visualTransformation = if (isPassword) PasswordVisualTransformation()
                else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    CalorAiTheme { 
        SignUpScreen(
            state = SignUpUiState(),
            onEvent = {}
        )
    }
}