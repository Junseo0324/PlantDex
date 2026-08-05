package com.devhjs.plantdex.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devhjs.plantdex.domain.usecase.ObserveDexSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeDexSummary: ObserveDexSummaryUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        fetchDexSummary()
    }

    private fun fetchDexSummary() {
        viewModelScope.launch {
            observeDexSummary(recentLimit = RECENT_LIMIT).collect { summary ->
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
