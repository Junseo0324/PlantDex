package com.devhjs.plantdex.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devhjs.plantdex.domain.usecase.GetDexCollectionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val getDexCollection: GetDexCollectionUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionState())
    val state = _state.asStateFlow()

    init {
        fetchCollection()
    }

    fun onAction(action: CollectionAction) {
        when (action) {
            is CollectionAction.QueryChanged -> _state.update { it.copy(query = action.query) }
            is CollectionAction.FilterChanged -> _state.update { it.copy(filter = action.filter) }
            is CollectionAction.OpenDetail -> Unit
        }
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    private fun fetchCollection() {
        viewModelScope.launch {
            _state
                .map { it.query to it.filter }
                .distinctUntilChanged()
                .flatMapLatest { (query, filter) ->
                    getDexCollection(query, filter == CollectionFilter.Favorites)
                }
                .collect { collection ->
                    _state.update {
                        it.copy(
                            entries = collection.entries,
                            totalCount = collection.totalCount,
                            lastDiscoveredAt = collection.lastDiscoveredAt,
                        )
                    }
                }
        }
    }
}
