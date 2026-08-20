package com.devhjs.plantdex.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devhjs.plantdex.R
import com.devhjs.plantdex.presentation.component.MemoEditorDialog

@Composable
fun DetailScreenRoot(
    entryId: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(entryId) {
        viewModel.onAction(DetailAction.Load(entryId))
    }

    LaunchedEffect(viewModel) {
        viewModel.event.collect { event ->
            when (event) {
                DetailEvent.NavigateBack -> onBack()
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier,
    )

    // 상태는 ViewModel 이 들고, 화면은 순수 UI 로 두기 위해 다이얼로그만 여기서 띄운다.
    if (state.isMemoEditorOpen) {
        MemoEditorDialog(
            value = state.memoDraft,
            onValueChange = { viewModel.onAction(DetailAction.MemoDraftChanged(it)) },
            onConfirm = { viewModel.onAction(DetailAction.SaveMemo) },
            onDismiss = { viewModel.onAction(DetailAction.DismissMemoEditor) },
            title = stringResource(R.string.memo_dialog_title),
            placeholder = stringResource(R.string.memo_dialog_placeholder),
            confirmLabel = stringResource(R.string.memo_dialog_save),
            dismissLabel = stringResource(R.string.memo_dialog_cancel),
        )
    }
}
