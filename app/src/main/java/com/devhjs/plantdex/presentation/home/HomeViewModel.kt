package com.devhjs.plantdex.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devhjs.plantdex.domain.usecase.GetDexSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDexSummary: GetDexSummaryUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<HomeEvent>()
    val event = _event.asSharedFlow()

    init {
        fetchDexSummary()
    }

    fun onAction(action: HomeAction) {
        val event = when (action) {
            HomeAction.Discover -> HomeEvent.NavigateToCamera
            HomeAction.SeeAllCollection -> HomeEvent.NavigateToCollection
            is HomeAction.OpenDetail -> HomeEvent.NavigateToDetail(action.entryId)
        }
        viewModelScope.launch { _event.emit(event) }
    }

    private fun fetchDexSummary() {
        viewModelScope.launch {
            getDexSummary(recentLimit = RECENT_LIMIT).collect { summary ->
                _state.update {
                    it.copy(
                        discoveredCount = summary.total,
                        thisMonthCount = summary.thisMonth,
                        recent = summary.recent,
                    )
                }
            }
        }
    }
}
