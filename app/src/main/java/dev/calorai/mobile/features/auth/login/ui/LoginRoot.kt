package dev.calorai.mobile.features.auth.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.PrimaryButton
import dev.calorai.mobile.core.uikit.PrimaryTextFieldWithTitle
import dev.calorai.mobile.core.uikit.commonGradientBackground
import dev.calorai.mobile.core.utils.ObserveAsEvents
import dev.calorai.mobile.features.auth.login.ui.LoginUiEvent.GoogleCredentials
import dev.calorai.mobile.features.auth.login.ui.LoginUiEvent.GoogleError
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.security.SecureRandom
import java.util.Base64


@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.uiActions) { action ->
        when (action) {
            LoginUiAction.GoogleAuth -> {
                val signInWithGoogleOption: GetSignInWithGoogleOption = GetSignInWithGoogleOption
                    .Builder(serverClientId = context.getString(R.string.web_client_id))
                    .setNonce(generateSecureRandomNonce())
                    .build()

                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(signInWithGoogleOption)
                    .build()

                coroutineScope.launch {
                    val credentialManager = CredentialManager.create(context)
                    try {
                        val result = credentialManager.getCredential(context, request)
                        viewModel.onEvent(GoogleCredentials(result.credential))
                    } catch (exception: Exception) {
                        viewModel.onEvent(GoogleError(exception))
                    }
                }
            }

            LoginUiAction.ShowErrorMessage ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = context.getString(R.string.something_went_wrong)
                    )
                }
        }
    }

    LoginScreen(
        state = state,
        hostState = snackbarHostState,
        onEvent = viewModel::onEvent,
    )
}

@Composable
private fun LoginScreen(
    state: LoginUiState,
    hostState: SnackbarHostState,
    onEvent: (LoginUiEvent) -> Unit = {},
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = hostState,
                modifier = Modifier.padding(bottom = 32.dp),
            )
        },
        containerColor = Color.Transparent,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .commonGradientBackground()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp,
                ),
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
                Spacer(Modifier.size(51.dp))
                PrimaryTextFieldWithTitle(
                    title = stringResource(R.string.auth_label_email),
                    placeholder = stringResource(R.string.auth_placeholder_email),
                    value = state.email,
                    onValueChange = { onEvent(LoginUiEvent.EmailChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(Modifier.size(20.dp))
                PrimaryTextFieldWithTitle(
                    title = stringResource(R.string.auth_label_password),
                    placeholder = stringResource(R.string.auth_placeholder_password),
                    value = state.password,
                    onValueChange = { onEvent(LoginUiEvent.PasswordChanged(it)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(Modifier.size(12.dp))
                Divider()
                Spacer(Modifier.size(12.dp))
                Button(
                    onClick = { onEvent(LoginUiEvent.GoogleLoginClick) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(25)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(R.string.auth_continue_with_google),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(Modifier.weight(1f))
                PrimaryButton(
                    onClick = { onEvent(LoginUiEvent.LoginButtonClick) },
                    text = stringResource(R.string.auth_continue),
                )
                Spacer(Modifier.size(9.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.auth_no_account),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.size(3.dp))
                    Text(
                        text = stringResource(R.string.auth_registration),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.clickable {
                            onEvent(LoginUiEvent.RegisterClick)
                        }
                    )
                }
                Spacer(Modifier.size(9.dp))
            }
        }
    }
}

private fun generateSecureRandomNonce(byteLength: Int = 32): String {
    val randomBytes = ByteArray(byteLength)
    SecureRandom.getInstanceStrong().nextBytes(randomBytes)
    return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)
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
private fun Divider() {
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
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    CalorAiTheme {
        LoginScreen(
            state = LoginUiState(),
            hostState = remember { SnackbarHostState() },
            onEvent = {},
        )
    }
}