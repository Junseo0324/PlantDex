package com.devhjs.plantdex.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devhjs.plantdex.domain.usecase.GetDexEntryUseCase
import com.devhjs.plantdex.domain.usecase.SaveMemoUseCase
import com.devhjs.plantdex.domain.usecase.SetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getDexEntry: GetDexEntryUseCase,
    private val setFavorite: SetFavoriteUseCase,
    private val saveMemo: SaveMemoUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<DetailEvent>()
    val event = _event.asSharedFlow()

    private var loadedId: Long? = null
    private var observeJob: Job? = null

    fun onAction(action: DetailAction) {
        when (action) {
            is DetailAction.Load -> load(action.entryId)
            DetailAction.ToggleFavorite -> toggleFavorite()
            DetailAction.OpenMemoEditor -> openMemoEditor()
            DetailAction.DismissMemoEditor -> closeMemoEditor()
            is DetailAction.MemoDraftChanged -> _state.update { it.copy(memoDraft = action.text) }
            DetailAction.SaveMemo -> submitMemo()
            DetailAction.Back -> viewModelScope.launch { _event.emit(DetailEvent.NavigateBack) }
        }
    }

    /** 탭을 오갔다 돌아와도 ViewModel 이 살아있어 Root 가 다시 보내므로, 같은 id 면 넘긴다. */
    private fun load(entryId: Long) {
        if (loadedId == entryId) return
        loadedId = entryId

        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            getDexEntry(entryId).collect { entry ->
                _state.update { it.copy(entry = entry) }
            }
        }
    }

    private fun toggleFavorite() {
        val entry = _state.value.entry ?: return
        viewModelScope.launch { setFavorite(entry.id, !entry.isFavorite) }
    }

    private fun openMemoEditor() {
        _state.update { it.copy(isMemoEditorOpen = true, memoDraft = it.entry?.memo.orEmpty()) }
    }

    private fun closeMemoEditor() {
        _state.update { it.copy(isMemoEditorOpen = false, memoDraft = "") }
    }

    private fun submitMemo() {
        val entry = _state.value.entry ?: return
        val draft = _state.value.memoDraft
        viewModelScope.launch { saveMemo(entry.id, draft) }
        closeMemoEditor()
    }
}
