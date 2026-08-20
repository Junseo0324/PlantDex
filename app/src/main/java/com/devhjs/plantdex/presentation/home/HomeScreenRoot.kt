package com.devhjs.plantdex.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun HomeScreenRoot(
    onDiscover: () -> Unit,
    onSeeAllCollection: () -> Unit,
    onOpenDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.event.collect { event ->
            when (event) {
                HomeEvent.NavigateToCamera -> onDiscover()
                HomeEvent.NavigateToCollection -> onSeeAllCollection()
                is HomeEvent.NavigateToDetail -> onOpenDetail(event.entryId)
            }
        }
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}
