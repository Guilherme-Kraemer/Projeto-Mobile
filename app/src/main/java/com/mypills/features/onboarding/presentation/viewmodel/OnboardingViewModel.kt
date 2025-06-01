package com.mypills.features.onboarding.presentation.viewmodel

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.mypills.core.settings.AppPreferences

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    @OptIn(ExperimentalFoundationApi::class)
    fun nextPage(pagerState: PagerState) {
        viewModelScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    fun previousPage(pagerState: PagerState) {
        viewModelScope.launch {
            pagerState.animateScrollToPage(pagerState.currentPage - 1)
        }
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            appPreferences.setFirstTime(false)
        }
    }
}