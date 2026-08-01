package com.devhjs.plantdex.presentation.analyze

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Hilt 를 아는 얇은 래퍼. 화면 자체는 stateless 로 남겨 preview 가 가능하게 한다.
 */
@Composable
fun AnalyzeScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: AnalyzeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AnalyzeScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}
