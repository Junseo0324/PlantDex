package com.devhjs.plantdex.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DetailScreenRoot(
    entryId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(entryId) { viewModel.load(entryId) }

    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailScreen(
        state = state,
        onAction = { action ->
            when (action) {
                DetailAction.Back -> onBack()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}
