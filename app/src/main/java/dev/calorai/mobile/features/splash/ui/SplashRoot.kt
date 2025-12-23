package dev.calorai.mobile.features.splash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.commonGradientBackground
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashRoot(
    viewModel: SplashViewModel = koinViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.beginNavigation()
    }
    SplashScreen()
}

@Composable
private fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .commonGradientBackground(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_kcal),
            contentDescription = "Icon",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(128.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        CircularProgressIndicator()
    }
}


@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    CalorAiTheme {
        SplashScreen()
    }
}