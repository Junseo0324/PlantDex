package com.devhjs.plantdex.presentation.analyze

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * Hilt 를 아는 얇은 래퍼. 화면 자체는 stateless 로 남겨 preview 가 가능하게 한다.
 */
@Composable
fun AnalyzeScreenRoot(
    photoUri: String,
    onRegistered: (Long) -> Unit,
    onRetake: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AnalyzeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(photoUri) {
        viewModel.onAction(AnalyzeAction.Start(photoUri))
    }

    // 이동은 한 번만 일어나야 해서 상태가 아니라 이벤트로 받는다.
    LaunchedEffect(viewModel) {
        viewModel.event.collect { event ->
            when (event) {
                is AnalyzeEvent.Registered -> onRegistered(event.entryId)
                AnalyzeEvent.Retake -> onRetake()
            }
        }
    }

    AnalyzeScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}
