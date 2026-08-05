package com.devhjs.plantdex.presentation.home

import androidx.compose.runtime.Composable
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

    HomeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                HomeAction.Discover -> onDiscover()
                HomeAction.SeeAllCollection -> onSeeAllCollection()
                is HomeAction.OpenDetail -> onOpenDetail(action.entryId)
            }
        },
        modifier = modifier,
    )
}
