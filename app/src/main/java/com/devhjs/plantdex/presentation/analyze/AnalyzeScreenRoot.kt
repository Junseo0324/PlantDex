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
    onRegistered: (Long) -> Unit,
    onRetake: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AnalyzeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // 등록 후 이동은 한 번만 일어나야 해서 상태가 아니라 이벤트로 받는다.
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is AnalyzeEvent.Registered -> onRegistered(event.entryId)
            }
        }
    }

    AnalyzeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                AnalyzeAction.Retake -> onRetake()
                else -> viewModel.onAction(action)
            }
        },
        modifier = modifier,
    )
}
