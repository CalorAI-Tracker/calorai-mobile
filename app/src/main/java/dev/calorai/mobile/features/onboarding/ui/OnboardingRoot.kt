package dev.calorai.mobile.features.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.calorai.mobile.R
import dev.calorai.mobile.core.uikit.CalorAiTheme
import dev.calorai.mobile.core.uikit.LabeledTextField
import dev.calorai.mobile.core.uikit.PrimaryButton
import dev.calorai.mobile.core.uikit.SimpleDropdown
import dev.calorai.mobile.core.uikit.commonGradientBackground
import dev.calorai.mobile.core.utils.ObserveAsEvents
import dev.calorai.mobile.features.onboarding.ui.OnboardingPage.Companion.NUM_PAGES
import dev.calorai.mobile.features.profile.ui.model.ActivityUi
import dev.calorai.mobile.features.profile.ui.model.GenderUi
import dev.calorai.mobile.features.profile.ui.model.GoalUi
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun OnboardingRoot(
    viewModel: OnboardingViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val animationScope = rememberCoroutineScope()
    ObserveAsEvents(viewModel.uiActions) { action ->
        when (action) {
            is OnboardingUiAction.ScrollToPage -> animationScope.launch {
                viewModel.pagerState.animateScrollToPage(action.page)
            }
        }
    }
    OnboardingScreen(
        state = state,
        pagerState = viewModel.pagerState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun OnboardingScreen(
    state: OnboardingUiState,
    pagerState: PagerState,
    onEvent: (OnboardingUiEvent) -> Unit = {}
) {
    val system = WindowInsets.systemBars.asPaddingValues()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .commonGradientBackground()
            .padding(
                top = system.calculateTopPadding() + 32.dp,
                bottom = system.calculateBottomPadding() + 32.dp,
                start = 16.dp,
                end = 16.dp,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            Icon(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                    .size(40.dp)
                    .clickable { onEvent(OnboardingUiEvent.BackButtonClick) }
                    .padding(12.dp),
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = "Back",
            )
            LinearProgressIndicator(
                progress = { (pagerState.currentPage + 1) / NUM_PAGES.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp),
                color = MaterialTheme.colorScheme.onSurface,
                gapSize = 0.dp,
                drawStopIndicator = {},
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            when (page) {
                OnboardingPage.GenderPage.INDEX -> OnboardingGenderPage(
                    state = state.genderPage,
                    onEvent = onEvent
                )

                OnboardingPage.HeightWeightPage.INDEX -> OnboardingHeightWeightPage(
                    state = state.heightWeightPage,
                    onEvent = onEvent
                )

                OnboardingPage.BirthdayPage.INDEX -> OnboardingBirthdayPage(
                    state = state.birthdayPage,
                    onEvent = onEvent
                )

                OnboardingPage.ActivityPage.INDEX -> OnboardingActivityPage(
                    state = state.activityPage,
                    onEvent = onEvent
                )

                OnboardingPage.GoalPage.INDEX -> OnboardingGoalPage(
                    state = state.goalPage,
                    onEvent = onEvent
                )
            }
        }
        PrimaryButton(
            onClick = { onEvent(OnboardingUiEvent.NextButtonClick) },
            text = stringResource(R.string.auth_continue),
        )
    }
}

@Composable
private fun OnboardingGoalPage(
    state: OnboardingPage.GoalPage,
    onEvent: (OnboardingUiEvent) -> Unit = {},
) {
    val resources = LocalResources.current
    OnboardingPage(title = stringResource(R.string.onboarding_goal_title)) {
        SimpleDropdown(
            label = stringResource(R.string.settings_activity_goal),
            placeholder = stringResource(R.string.settings_activity_goal),
            options = GoalUi.entries,
            showLabel = false,
            selected = state.goal,
            mapToString = { resources.getString(it.labelResId) },
            onSelected = { onEvent(OnboardingUiEvent.GoalChange(it)) }
        )
    }
}

@Composable
private fun OnboardingActivityPage(
    state: OnboardingPage.ActivityPage,
    onEvent: (OnboardingUiEvent) -> Unit = {},
) {
    val resources = LocalResources.current
    OnboardingPage(title = stringResource(R.string.onboarding_activity_title)) {
        SimpleDropdown(
            label = stringResource(R.string.settings_activity),
            placeholder = stringResource(R.string.settings_activity),
            options = ActivityUi.entries,
            showLabel = false,
            selected = state.activity,
            mapToString = { resources.getString(it.labelResId) },
            onSelected = { onEvent(OnboardingUiEvent.ActivityChange(it)) }
        )
    }
}

@Composable
private fun OnboardingBirthdayPage(
    state: OnboardingPage.BirthdayPage,
    onEvent: (OnboardingUiEvent) -> Unit = {},
) {
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    OnboardingPage(
        title = stringResource(R.string.onboarding_birthday_title),
        description = stringResource(R.string.onboarding_birthday_description),
    ) {
        LabeledTextField(
            value = state.birthDate,
            showLabel = false,
            label = stringResource(R.string.settings_birthday),
            readOnly = true,
            onClick = { showDatePicker = true },
        )
    }
    if (showDatePicker) {
        BirthDayPicker(
            setShowState = { showDatePicker = it },
            datePickerState = datePickerState,
            onEvent = onEvent,
        )
    }
}

@Composable
private fun BirthDayPicker(
    setShowState: (Boolean) -> Unit,
    datePickerState: DatePickerState,
    onEvent: (OnboardingUiEvent) -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = { setShowState(false) },
        confirmButton = {
            Button(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onEvent(OnboardingUiEvent.BirthDateChange(it))
                    }
                    setShowState(false)
                }
            ) { Text(stringResource(R.string.settings_date_picker_apply)) }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun OnboardingGenderPage(
    state: OnboardingPage.GenderPage,
    onEvent: (OnboardingUiEvent) -> Unit = {},
) {
    val resources = LocalResources.current

    OnboardingPage(
        title = stringResource(R.string.onboarding_gender_title),
        description = stringResource(R.string.onboarding_gender_description),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SimpleDropdown(
                label = stringResource(R.string.settings_sex),
                placeholder = stringResource(R.string.settings_sex),
                options = GenderUi.entries,
                selected = state.gender,
                showLabel = false,
                mapToString = { resources.getString(it.labelResId) },
                onSelected = { onEvent(OnboardingUiEvent.GenderChange(it)) },
            )
        }
    }
}

@Composable
private fun OnboardingHeightWeightPage(
    state: OnboardingPage.HeightWeightPage,
    onEvent: (OnboardingUiEvent) -> Unit = {},
) {
    val resources = LocalResources.current
    var heightOptions by remember { mutableStateOf((140..210 step 5).toList()) }
    var weightOptions by remember { mutableStateOf((40..140 step 5).toList()) }

    OnboardingPage(
        title = stringResource(R.string.onboarding_height_weight_title),
        description = stringResource(R.string.onboarding_height_weight_description),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            SimpleDropdown(
                label = stringResource(R.string.settings_height),
                placeholder = stringResource(R.string.settings_height_val),
                options = heightOptions,
                showLabel = false,
                selected = state.height,
                mapToString = { resources.getString(R.string.settings_height_val_type, it) },
                onSelected = { onEvent(OnboardingUiEvent.HeightChange(it)) },
            )
            SimpleDropdown(
                label = stringResource(R.string.settings_weight),
                placeholder = stringResource(R.string.settings_weight_val),
                options = weightOptions,
                showLabel = false,
                selected = state.weight,
                mapToString = { resources.getString(R.string.settings_weight_val_type, it) },
                onSelected = { onEvent(OnboardingUiEvent.WeightChange(it)) },
            )
        }
    }
}

@Composable
private fun OnboardingPage(
    title: String,
    description: String? = null,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
        )
        if (!description.isNullOrEmpty()) {
            Spacer(Modifier.size(5.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Spacer(Modifier.size(20.dp))
        content()
    }
}

@Composable
private fun OnboardingScreenPagePreview(pageIndex: Int) {
    CalorAiTheme {
        OnboardingScreen(
            state = OnboardingUiState(),
            pagerState = PagerState(currentPage = pageIndex) { NUM_PAGES },
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenGenderPagePreview() {
    OnboardingScreenPagePreview(OnboardingPage.GenderPage.INDEX)
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenHeightWeightPreview() {
    OnboardingScreenPagePreview(OnboardingPage.HeightWeightPage.INDEX)
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenBirthdayPagePreview() {
    OnboardingScreenPagePreview(OnboardingPage.BirthdayPage.INDEX)
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenActivityPagePreview() {
    OnboardingScreenPagePreview(OnboardingPage.ActivityPage.INDEX)
}

@Preview(showBackground = true)
@Composable
private fun OnboardingScreenGoalPagePreview() {
    OnboardingScreenPagePreview(OnboardingPage.GoalPage.INDEX)
}