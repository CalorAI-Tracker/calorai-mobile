package dev.calorai.mobile.features.onboarding.ui

import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.toRoute
import dev.calorai.mobile.core.navigation.Router
import dev.calorai.mobile.features.auth.signUp.navigateToSignUpScreen
import dev.calorai.mobile.features.main.navigateToMainScreen
import dev.calorai.mobile.features.onboarding.OnboardingRoute
import dev.calorai.mobile.features.onboarding.data.OnboardingMapper
import dev.calorai.mobile.features.profile.domain.CreateUserProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId

class OnboardingViewModel constructor(
    savedStateHandle: SavedStateHandle,
    private val globalRouter: Router,
    private val createUserProfileUseCase: CreateUserProfileUseCase,
    private val mapper: OnboardingMapper,
) : ViewModel() {

    private val onboardingRoute = savedStateHandle.toRoute<OnboardingRoute>()

    private val _state = MutableStateFlow(OnboardingUiState())
    val state: StateFlow<OnboardingUiState> = _state
    val pagerState: PagerState = PagerState(
        currentPage = 0,
        pageCount = { OnboardingPage.NUM_PAGES },
    )

    private val _uiActions = MutableSharedFlow<OnboardingUiAction>()
    val uiActions: SharedFlow<OnboardingUiAction> = _uiActions.asSharedFlow()

    fun onEvent(event: OnboardingUiEvent) {
        when (event) {
            OnboardingUiEvent.NextButtonClick -> handleNextButtonClick()
            is OnboardingUiEvent.ActivityChange -> launchUpdateActivityPage { copy(activity = event.value) }
            is OnboardingUiEvent.BirthDateChange -> launchUpdateBirthdayPagePage {
                val instant = Instant.ofEpochMilli(event.selectedDateMillis)
                val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                copy(birthDate = mapper.mapLocalDateToUi(localDate))
            }

            is OnboardingUiEvent.GenderChange -> launchUpdateGenderPage { copy(gender = event.value) }
            is OnboardingUiEvent.GoalChange -> launchUpdateGoalPage { copy(goal = event.value) }
            is OnboardingUiEvent.HeightChange -> launchUpdateHeightWeightPage { copy(height = event.value) }
            is OnboardingUiEvent.WeightChange -> launchUpdateHeightWeightPage { copy(weight = event.value) }
            OnboardingUiEvent.BackButtonClick -> handleBackButtonClick()
        }
    }

    private fun handleBackButtonClick() {
        val currentPage = pagerState.currentPage
        if (currentPage == 0) {
            navigateToSignUp()
        } else {
            updatePageNumber(currentPage - 1)
        }
    }

    private fun handleNextButtonClick() {
        val currentPage = pagerState.currentPage
        if (currentPage != OnboardingPage.NUM_PAGES - 1) {
            updatePageNumber(currentPage + 1)
            return
        }

        viewModelScope.launch {
            val currentState = _state.value
            runCatching {
                createUserProfileUseCase.invoke(
                    payload = mapper.mapToDomain(
                        onboardingUiState = currentState,
                        name = onboardingRoute.name,
                    )
                )
            }.onSuccess {
                globalRouter.emit {
                    navigateToMainScreen(
                        navOptions = NavOptions.Builder()
                            .setPopUpTo<OnboardingRoute>(inclusive = true)
                            .build()
                    )
                }
            }
        }
    }

    private fun updatePageNumber(pageNumber: Int) {
        viewModelScope.launch {
            _uiActions.emit(OnboardingUiAction.ScrollToPage(pageNumber))
        }
    }

    private fun launchUpdateGenderPage(
        block: OnboardingPage.GenderPage.() -> OnboardingPage.GenderPage
    ) {
        viewModelScope.launch {
            _state.update { it.copy(genderPage = it.genderPage.block()) }
        }
    }

    private fun launchUpdateGoalPage(
        block: OnboardingPage.GoalPage.() -> OnboardingPage.GoalPage
    ) {
        viewModelScope.launch {
            _state.update { it.copy(goalPage = it.goalPage.block()) }
        }
    }

    private fun launchUpdateActivityPage(
        block: OnboardingPage.ActivityPage.() -> OnboardingPage.ActivityPage
    ) {
        viewModelScope.launch {
            _state.update { it.copy(activityPage = it.activityPage.block()) }
        }
    }

    private fun launchUpdateHeightWeightPage(
        block: OnboardingPage.HeightWeightPage.() -> OnboardingPage.HeightWeightPage
    ) {
        viewModelScope.launch {
            _state.update { it.copy(heightWeightPage = it.heightWeightPage.block()) }
        }
    }

    private fun launchUpdateBirthdayPagePage(
        block: OnboardingPage.BirthdayPage.() -> OnboardingPage.BirthdayPage
    ) {
        viewModelScope.launch {
            _state.update { it.copy(birthdayPage = it.birthdayPage.block()) }
        }
    }

    private fun navigateToSignUp() {
        viewModelScope.launch {
            globalRouter.emit {
                navigateToSignUpScreen(
                    navOptions = NavOptions.Builder()
                        .setPopUpTo<OnboardingRoute>(inclusive = true)
                        .build()
                )
            }
        }
    }
}
