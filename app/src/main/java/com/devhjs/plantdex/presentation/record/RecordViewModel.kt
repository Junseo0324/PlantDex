package com.devhjs.plantdex.presentation.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devhjs.plantdex.domain.usecase.GetDexStatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordViewModel @Inject constructor(
    private val getDexStats: GetDexStatsUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(RecordState())
    val state = _state.asStateFlow()

    init {
        fetchDexStats()
    }

    private fun fetchDexStats() {
        viewModelScope.launch {
            getDexStats().collect { stats ->
                _state.update { it.copy(stats = stats) }
            }
        }
    }
}
