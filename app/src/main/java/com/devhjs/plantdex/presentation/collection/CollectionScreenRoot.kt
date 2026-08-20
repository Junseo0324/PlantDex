package com.devhjs.plantdex.presentation.collection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CollectionScreenRoot(
    onOpenDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CollectionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.event.collect { event ->
            when (event) {
                is CollectionEvent.NavigateToDetail -> onOpenDetail(event.entryId)
            }
        }
    }

    CollectionScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}
